/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.transaction.interceptor;

import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.ConcurrentMap;

import io.vavr.control.Try;
import kotlin.reflect.KFunction;
import kotlin.reflect.jvm.ReflectJvmMapping;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.core.KotlinDetector;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.lang.Nullable;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.ReactiveTransaction;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.TransactionUsageException;
import org.springframework.transaction.reactive.TransactionContextManager;
import org.springframework.transaction.support.CallbackPreferringPlatformTransactionManager;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.StringUtils;

/**
 * Base class for transactional aspects, such as the {@link TransactionInterceptor}
 * or an AspectJ aspect.
 *
 * <p>This enables the underlying Spring transaction infrastructure to be used easily
 * to implement an aspect for any aspect system.
 *
 * <p>Subclasses are responsible for calling methods in this class in the correct order.
 *
 * <p>If no transaction name has been specified in the {@link TransactionAttribute},
 * the exposed name will be the {@code fully-qualified class name + "." + method name}
 * (by default).
 *
 * <p>Uses the <b>Strategy</b> design pattern. A {@link PlatformTransactionManager} or
 * {@link ReactiveTransactionManager} implementation will perform the actual transaction
 * management, and a {@link TransactionAttributeSource} (e.g. annotation-based) is used
 * for determining transaction definitions for a particular class or method.
 *
 * <p>A transaction aspect is serializable if its {@code TransactionManager} and
 * {@code TransactionAttributeSource} are serializable.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Stéphane Nicoll
 * @author Sam Brannen
 * @author Mark Paluch
 * @since 1.1
 * @see PlatformTransactionManager
 * @see ReactiveTransactionManager
 * @see #setTransactionManager
 * @see #setTransactionAttributes
 * @see #setTransactionAttributeSource
 */
public abstract class TransactionAspectSupport implements BeanFactoryAware, InitializingBean {

	// NOTE: This class must not implement Serializable because it serves as base
	// class for AspectJ aspects (which are not allowed to implement Serializable)!


	/**
	 * Key to use to store the default transaction manager.
	 * 作为存储默认transaction manager的key
	 */
	private static final Object DEFAULT_TRANSACTION_MANAGER_KEY = new Object();

	/**
	 * Vavr library present on the classpath?
	 * 类路径上是否存在Vavr库？
	 */
	private static final boolean vavrPresent = ClassUtils.isPresent(
			"io.vavr.control.Try", TransactionAspectSupport.class.getClassLoader());

	/**
	 * Reactive Streams API present on the classpath?
	 * 类路径上是否存在反应流API？
	 */
	private static final boolean reactiveStreamsPresent =
			ClassUtils.isPresent("org.reactivestreams.Publisher", TransactionAspectSupport.class.getClassLoader());

	/**
	 * Holder to support the {@code currentTransactionStatus()} method,
	 * and to support communication between different cooperating advices
	 * (e.g. before and after advice) if the aspect involves more than a
	 * single method (as will be the case for around advice).
	 */
	private static final ThreadLocal<TransactionInfo> transactionInfoHolder =
			new NamedThreadLocal<>("Current aspect-driven transaction");


	/**
	 * Subclasses can use this to return the current TransactionInfo.
	 * Only subclasses that cannot handle all operations in one method,
	 * such as an AspectJ aspect involving distinct before and after advice,
	 * need to use this mechanism to get at the current TransactionInfo.
	 * An around advice such as an AOP Alliance MethodInterceptor can hold a
	 * reference to the TransactionInfo throughout the aspect method.
	 * <p>A TransactionInfo will be returned even if no transaction was created.
	 * The {@code TransactionInfo.hasTransaction()} method can be used to query this.
	 * <p>To find out about specific transaction characteristics, consider using
	 * TransactionSynchronizationManager's {@code isSynchronizationActive()}
	 * and/or {@code isActualTransactionActive()} methods.
	 * @return the TransactionInfo bound to this thread, or {@code null} if none
	 * @see TransactionInfo#hasTransaction()
	 * @see org.springframework.transaction.support.TransactionSynchronizationManager#isSynchronizationActive()
	 * @see org.springframework.transaction.support.TransactionSynchronizationManager#isActualTransactionActive()
	 */
	@Nullable
	protected static TransactionInfo currentTransactionInfo() throws NoTransactionException {
		return transactionInfoHolder.get();
	}

	/**
	 * Return the transaction status of the current method invocation.
	 * Mainly intended for code that wants to set the current transaction
	 * rollback-only but not throw an application exception.
	 * @throws NoTransactionException if the transaction info cannot be found,
	 * because the method was invoked outside an AOP invocation context
	 */
	public static TransactionStatus currentTransactionStatus() throws NoTransactionException {
		TransactionInfo info = currentTransactionInfo();
		if (info == null || info.transactionStatus == null) {
			throw new NoTransactionException("No transaction aspect-managed TransactionStatus in scope");
		}
		return info.transactionStatus;
	}


	protected final Log logger = LogFactory.getLog(getClass());

	@Nullable
	private final ReactiveAdapterRegistry reactiveAdapterRegistry;

	/**
	 * 事务管理器的beanName
	 */
	@Nullable
	private String transactionManagerBeanName;

	/**
	 * 事务管理器
	 */
	@Nullable
	private TransactionManager transactionManager;

	/**
	 * 事务属性Source
	 */
	@Nullable
	private TransactionAttributeSource transactionAttributeSource;

	@Nullable
	private BeanFactory beanFactory;

	private final ConcurrentMap<Object, TransactionManager> transactionManagerCache =
			new ConcurrentReferenceHashMap<>(4);

	private final ConcurrentMap<Method, ReactiveTransactionSupport> transactionSupportCache =
			new ConcurrentReferenceHashMap<>(1024);


	protected TransactionAspectSupport() {
		if (reactiveStreamsPresent) {
			this.reactiveAdapterRegistry = ReactiveAdapterRegistry.getSharedInstance();
		}
		else {
			this.reactiveAdapterRegistry = null;
		}
	}


	/**
	 * Specify the name of the default transaction manager bean.
	 * This can either point to a traditional {@link PlatformTransactionManager} or a
	 * {@link ReactiveTransactionManager} for reactive transaction management.
	 */
	public void setTransactionManagerBeanName(@Nullable String transactionManagerBeanName) {
		this.transactionManagerBeanName = transactionManagerBeanName;
	}

	/**
	 * Return the name of the default transaction manager bean.
	 */
	@Nullable
	protected final String getTransactionManagerBeanName() {
		return this.transactionManagerBeanName;
	}

	/**
	 * Specify the <em>default</em> transaction manager to use to drive transactions.
	 * This can either be a traditional {@link PlatformTransactionManager} or a
	 * {@link ReactiveTransactionManager} for reactive transaction management.
	 * <p>The default transaction manager will be used if a <em>qualifier</em>
	 * has not been declared for a given transaction or if an explicit name for the
	 * default transaction manager bean has not been specified.
	 * @see #setTransactionManagerBeanName
	 */
	public void setTransactionManager(@Nullable TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	/**
	 * Return the default transaction manager, or {@code null} if unknown.
	 * This can either be a traditional {@link PlatformTransactionManager} or a
	 * {@link ReactiveTransactionManager} for reactive transaction management.
	 */
	@Nullable
	public TransactionManager getTransactionManager() {
		return this.transactionManager;
	}

	/**
	 * Set properties with method names as keys and transaction attribute
	 * descriptors (parsed via TransactionAttributeEditor) as values:
	 * e.g. key = "myMethod", value = "PROPAGATION_REQUIRED,readOnly".
	 * <p>Note: Method names are always applied to the target class,
	 * no matter if defined in an interface or the class itself.
	 * <p>Internally, a NameMatchTransactionAttributeSource will be
	 * created from the given properties.
	 * @see #setTransactionAttributeSource
	 * @see TransactionAttributeEditor
	 * @see NameMatchTransactionAttributeSource
	 */
	/**
	 * 配置transactionAttributeSource
	 * @param transactionAttributes
	 *
	 * 这是一个NameMatchTransactionAttributeSource对象，同时把在IOC容器中设置的事务处理属性配置到这个transactionAttributeSource
	 * 中
	 */
	public void setTransactionAttributes(Properties transactionAttributes) {
		NameMatchTransactionAttributeSource tas = new NameMatchTransactionAttributeSource();
		tas.setProperties(transactionAttributes);
		this.transactionAttributeSource = tas;
	}

	/**
	 * Set multiple transaction attribute sources which are used to find transaction
	 * attributes. Will build a CompositeTransactionAttributeSource for the given sources.
	 * @see CompositeTransactionAttributeSource
	 * @see MethodMapTransactionAttributeSource
	 * @see NameMatchTransactionAttributeSource
	 * @see org.springframework.transaction.annotation.AnnotationTransactionAttributeSource
	 */
	public void setTransactionAttributeSources(TransactionAttributeSource... transactionAttributeSources) {
		this.transactionAttributeSource = new CompositeTransactionAttributeSource(transactionAttributeSources);
	}

	/**
	 * Set the transaction attribute source which is used to find transaction
	 * attributes. If specifying a String property value, a PropertyEditor
	 * will create a MethodMapTransactionAttributeSource from the value.
	 * @see TransactionAttributeSourceEditor
	 * @see MethodMapTransactionAttributeSource
	 * @see NameMatchTransactionAttributeSource
	 * @see org.springframework.transaction.annotation.AnnotationTransactionAttributeSource
	 */
	public void setTransactionAttributeSource(@Nullable TransactionAttributeSource transactionAttributeSource) {
		this.transactionAttributeSource = transactionAttributeSource;
	}

	/**
	 * Return the transaction attribute source.
	 * 返回事务属性source。
	 */
	@Nullable
	public TransactionAttributeSource getTransactionAttributeSource() {
		return this.transactionAttributeSource;
	}

	/**
	 * Set the BeanFactory to use for retrieving {@code TransactionManager} beans.
	 */
	@Override
	public void setBeanFactory(@Nullable BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	/**
	 * Return the BeanFactory to use for retrieving {@code TransactionManager} beans.
	 */
	@Nullable
	protected final BeanFactory getBeanFactory() {
		return this.beanFactory;
	}

	/**
	 * Check that required properties were set.
	 * 检查属性是否配置
	 */
	@Override
	public void afterPropertiesSet() {
		if (getTransactionManager() == null && this.beanFactory == null) {
			throw new IllegalStateException(
					"Set the 'transactionManager' property or make sure to run within a BeanFactory " +
					"containing a TransactionManager bean!");
		}
		if (getTransactionAttributeSource() == null) {
			throw new IllegalStateException(
					"Either 'transactionAttributeSource' or 'transactionAttributes' is required: " +
					"If there are no transactional methods, then don't use a transaction aspect.");
		}
	}


	/**
	 * General delegate for around-advice-based subclasses, delegating to several other template
	 * methods on this class. Able to handle {@link CallbackPreferringPlatformTransactionManager}
	 * as well as regular {@link PlatformTransactionManager} implementations and
	 * {@link ReactiveTransactionManager} implementations for reactive return types.
	 * @param method the Method being invoked
	 * @param targetClass the target class that we're invoking the method on
	 * @param invocation the callback to use for proceeding with the target invocation
	 * @return the return value of the method, if any
	 * @throws Throwable propagated from the target invocation
	 */
	@Nullable
	protected Object invokeWithinTransaction(Method method, @Nullable Class<?> targetClass,
			final InvocationCallback invocation) throws Throwable {

		// If the transaction attribute is null, the method is non-transactional.
		//这里读取事务的属性配置，通过TransactionAttributeSource对象取得
		TransactionAttributeSource tas = getTransactionAttributeSource();
		final TransactionAttribute txAttr = (tas != null ? tas.getTransactionAttribute(method, targetClass) : null);
		//获取具体的事务处理器
		final TransactionManager tm = determineTransactionManager(txAttr);

		if (this.reactiveAdapterRegistry != null && tm instanceof ReactiveTransactionManager) {
			ReactiveTransactionSupport txSupport = this.transactionSupportCache.computeIfAbsent(method, key -> {
				if (KotlinDetector.isKotlinType(method.getDeclaringClass()) && KotlinDelegate.isSuspend(method)) {
					throw new TransactionUsageException(
							"Unsupported annotated transaction on suspending function detected: " + method +
							". Use TransactionalOperator.transactional extensions instead.");
				}
				ReactiveAdapter adapter = this.reactiveAdapterRegistry.getAdapter(method.getReturnType());
				if (adapter == null) {
					throw new IllegalStateException("Cannot apply reactive transaction to non-reactive return type: " +
							method.getReturnType());
				}
				return new ReactiveTransactionSupport(adapter);
			});
			return txSupport.invokeWithinTransaction(
					method, targetClass, invocation, txAttr, (ReactiveTransactionManager) tm);
		}

		PlatformTransactionManager ptm = asPlatformTransactionManager(tm);
		final String joinpointIdentification = methodIdentification(method, targetClass, txAttr);

		//这里区分不同类型的PlatformTransactionManager，因为他们的调用方式不同
		//对CallbackPreferringPlatformTransactionManager来说，需要回调函数来实现事务的创建和提交。对于非
		//CallbackPreferringPlatformTransactionManager，不需要通过回调函数来实现事务的创建和提交
		//像DataSourceTransactionManager不需要通过回调的方式来使用
		if (txAttr == null || !(ptm instanceof CallbackPreferringPlatformTransactionManager)) {
			// Standard transaction demarcation with getTransaction and commit/rollback calls.
			/**这里创建事务，同时把创建事务过程中得到的信息放到TransactionInfo 中去，TransactionInfo是保存当前事务状态的对象*/
			TransactionInfo txInfo = createTransactionIfNecessary(ptm, txAttr, joinpointIdentification);

			Object retVal;
			try {
				// This is an around advice: Invoke the next interceptor in the chain.
				// This will normally result in a target object being invoked.
				//这里的调用使处理沿着拦截器链进行，使最后目标对象的方法的得到调用
				retVal = invocation.proceedWithInvocation();
			}
			catch (Throwable ex) {
				// target invocation exception
				//如果在事务处理方法调用中出现了异常，事务如何进行需要根据具体的情况考虑回滚或者提交
				completeTransactionAfterThrowing(txInfo, ex);
				throw ex;
			}
			finally {
				//这里把与线程绑定的TransactionInfo设置为oldTransactionInfo
				cleanupTransactionInfo(txInfo);
			}

			if (vavrPresent && VavrDelegate.isVavrTry(retVal)) {
				// Set rollback-only in case of Vavr failure matching our rollback rules...
				TransactionStatus status = txInfo.getTransactionStatus();
				if (status != null && txAttr != null) {
					retVal = VavrDelegate.evaluateTryFailure(retVal, txAttr, status);
				}
			}

			//TransactionInterceptor 的事务提交调用入口
			commitTransactionAfterReturning(txInfo);
			return retVal;
		}

		else {
			//采用回调的方法来使用事务处理器
			final ThrowableHolder throwableHolder = new ThrowableHolder();

			// It's a CallbackPreferringPlatformTransactionManager: pass a TransactionCallback in.
			//它是一个CallbackPreferringPlatformTransactionManager：传入TransactionCallback。
			try {
				Object result = ((CallbackPreferringPlatformTransactionManager) ptm).execute(txAttr, status -> {
					TransactionInfo txInfo = prepareTransactionInfo(ptm, txAttr, joinpointIdentification, status);
					try {
						Object retVal = invocation.proceedWithInvocation();
						if (vavrPresent && VavrDelegate.isVavrTry(retVal)) {
							// Set rollback-only in case of Vavr failure matching our rollback rules...
							retVal = VavrDelegate.evaluateTryFailure(retVal, txAttr, status);
						}
						return retVal;
					}
					catch (Throwable ex) {
						if (txAttr.rollbackOn(ex)) {
							// A RuntimeException: will lead to a rollback.
							//运行时异常：事务回滚
							if (ex instanceof RuntimeException) {
								throw (RuntimeException) ex;
							}
							else {
								throw new ThrowableHolderException(ex);
							}
						}
						else {
							// A normal return value: will lead to a commit.
							//正常返回：事务提交
							throwableHolder.throwable = ex;
							return null;
						}
					}
					finally {
						cleanupTransactionInfo(txInfo);
					}
				});

				// Check result state: It might indicate a Throwable to rethrow.
				if (throwableHolder.throwable != null) {
					throw throwableHolder.throwable;
				}
				return result;
			}
			catch (ThrowableHolderException ex) {
				throw ex.getCause();
			}
			catch (TransactionSystemException ex2) {
				if (throwableHolder.throwable != null) {
					logger.error("Application exception overridden by commit exception", throwableHolder.throwable);
					ex2.initApplicationException(throwableHolder.throwable);
				}
				throw ex2;
			}
			catch (Throwable ex2) {
				if (throwableHolder.throwable != null) {
					logger.error("Application exception overridden by commit exception", throwableHolder.throwable);
				}
				throw ex2;
			}
		}
	}

	/**
	 * Clear the cache.
	 */
	protected void clearTransactionManagerCache() {
		this.transactionManagerCache.clear();
		this.beanFactory = null;
	}

	/**
	 * Determine the specific transaction manager to use for the given transaction.
	 * 根据TransactionProxyFactoryBean的配置信息，确定要用于给定事务的特定事务管理器。
	 */
	@Nullable
	protected TransactionManager determineTransactionManager(@Nullable TransactionAttribute txAttr) {
		// Do not attempt to lookup tx manager if no tx attributes are set
		if (txAttr == null || this.beanFactory == null) {
			return getTransactionManager();
		}

		String qualifier = txAttr.getQualifier();
		if (StringUtils.hasText(qualifier)) {
			return determineQualifiedTransactionManager(this.beanFactory, qualifier);
		}
		else if (StringUtils.hasText(this.transactionManagerBeanName)) {
			return determineQualifiedTransactionManager(this.beanFactory, this.transactionManagerBeanName);
		}
		else {
			TransactionManager defaultTransactionManager = getTransactionManager();
			if (defaultTransactionManager == null) {
				defaultTransactionManager = this.transactionManagerCache.get(DEFAULT_TRANSACTION_MANAGER_KEY);
				if (defaultTransactionManager == null) {
					defaultTransactionManager = this.beanFactory.getBean(TransactionManager.class);
					this.transactionManagerCache.putIfAbsent(
							DEFAULT_TRANSACTION_MANAGER_KEY, defaultTransactionManager);
				}
			}
			return defaultTransactionManager;
		}
	}

	private TransactionManager determineQualifiedTransactionManager(BeanFactory beanFactory, String qualifier) {
		TransactionManager txManager = this.transactionManagerCache.get(qualifier);
		if (txManager == null) {
			txManager = BeanFactoryAnnotationUtils.qualifiedBeanOfType(
					beanFactory, TransactionManager.class, qualifier);
			this.transactionManagerCache.putIfAbsent(qualifier, txManager);
		}
		return txManager;
	}


	/**
	 * 作为PlatformTransactionManager事务管理器
	 * @param transactionManager
	 * @return
	 */
	@Nullable
	private PlatformTransactionManager asPlatformTransactionManager(@Nullable Object transactionManager) {
		if (transactionManager == null || transactionManager instanceof PlatformTransactionManager) {
			return (PlatformTransactionManager) transactionManager;
		}
		else {
			throw new IllegalStateException(
					"Specified transaction manager is not a PlatformTransactionManager: " + transactionManager);
		}
	}

	/**
	 * 方法标识
	 * @param method
	 * @param targetClass
	 * @param txAttr
	 * @return
	 */
	private String methodIdentification(Method method, @Nullable Class<?> targetClass,
			@Nullable TransactionAttribute txAttr) {

		String methodIdentification = methodIdentification(method, targetClass);
		if (methodIdentification == null) {
			if (txAttr instanceof DefaultTransactionAttribute) {
				methodIdentification = ((DefaultTransactionAttribute) txAttr).getDescriptor();
			}
			if (methodIdentification == null) {
				methodIdentification = ClassUtils.getQualifiedMethodName(method, targetClass);
			}
		}
		return methodIdentification;
	}

	/**
	 * Convenience method to return a String representation of this Method
	 * for use in logging. Can be overridden in subclasses to provide a
	 * different identifier for the given method.
	 * <p>The default implementation returns {@code null}, indicating the
	 * use of {@link DefaultTransactionAttribute#getDescriptor()} instead,
	 * ending up as {@link ClassUtils#getQualifiedMethodName(Method, Class)}.
	 * @param method the method we're interested in
	 * @param targetClass the class that the method is being invoked on
	 * @return a String representation identifying this method
	 * @see org.springframework.util.ClassUtils#getQualifiedMethodName
	 */
	@Nullable
	protected String methodIdentification(Method method, @Nullable Class<?> targetClass) {
		return null;
	}

	/**
	 * Create a transaction if necessary based on the given TransactionAttribute.
	 * <p>Allows callers to perform custom TransactionAttribute lookups through
	 * the TransactionAttributeSource.
	 * @param txAttr the TransactionAttribute (may be {@code null})
	 * @param joinpointIdentification the fully qualified method name
	 * (used for monitoring and logging purposes)
	 * @return a TransactionInfo object, whether or not a transaction was created.
	 * The {@code hasTransaction()} method on TransactionInfo can be used to
	 * tell if there was a transaction created.
	 * @see #getTransactionAttributeSource()
	 * 事务创建的入口
	 */
	@SuppressWarnings("serial")
	protected TransactionInfo createTransactionIfNecessary(@Nullable PlatformTransactionManager tm,
			@Nullable TransactionAttribute txAttr, final String joinpointIdentification) {

		// If no name specified, apply method identification as transaction name.
		//如果没有指定名称，则应用方法标识作为事务名称。
		if (txAttr != null && txAttr.getName() == null) {
			//创建事务属性委托类，并重写getName方法
			txAttr = new DelegatingTransactionAttribute(txAttr) {
				@Override
				public String getName() {
					return joinpointIdentification;
				}
			};
		}

		//这个TransactionStatus封装了事务执行的状态信息
		TransactionStatus status = null;
		if (txAttr != null) {
			if (tm != null) {
				//这里使用了定义好的事务方法的配置信息
				//事务创建由事务处理器来完成、同时返回TransactionStatus来记录当前的事务状态、包括已经创建的事务
				status = tm.getTransaction(txAttr);
			}
			else {
				if (logger.isDebugEnabled()) {
					logger.debug("Skipping transactional joinpoint [" + joinpointIdentification +
							"] because no transaction manager has been configured");
				}
			}
		}
		//准备TransactionInfo ,TransactionInfo 对象封装了事务处理的配置信息以及TransactionStatus
		return prepareTransactionInfo(tm, txAttr, joinpointIdentification, status);
	}

	/**
	 * Prepare a TransactionInfo for the given attribute and status object.
	 * 为给定的属性和状态对象准备TransactionInfo。
	 * @param txAttr the TransactionAttribute (may be {@code null})
	 * @param joinpointIdentification the fully qualified method name
	 * (used for monitoring and logging purposes)
	 * @param status the TransactionStatus for the current transaction
	 * @return the prepared TransactionInfo object
	 */
	protected TransactionInfo prepareTransactionInfo(@Nullable PlatformTransactionManager tm,
			@Nullable TransactionAttribute txAttr, String joinpointIdentification,
			@Nullable TransactionStatus status) {

		TransactionInfo txInfo = new TransactionInfo(tm, txAttr, joinpointIdentification);
		if (txAttr != null) {
			// We need a transaction for this method...
			if (logger.isTraceEnabled()) {
				logger.trace("Getting transaction for [" + txInfo.getJoinpointIdentification() + "]");
			}
			// The transaction manager will flag an error if an incompatible tx already exists.
			/*这里为TransactionInfo设置TransactionStatus，这个TransactionStatus很重要，它持有管理事务处理需要的数据，比如TransactionInfo对象就是由它持有的*/
			txInfo.newTransactionStatus(status);
		}
		else {
			// The TransactionInfo.hasTransaction() method will return false. We created it only
			// to preserve the integrity of the ThreadLocal stack maintained in this class.
			if (logger.isTraceEnabled()) {
				logger.trace("No need to create transaction for [" + joinpointIdentification +
						"]: This method is not transactional.");
			}
		}

		// We always bind the TransactionInfo to the thread, even if we didn't create
		// a new transaction here. This guarantees that the TransactionInfo stack
		// will be managed correctly even if no transaction was created by this aspect.
		//这里把当前的TransactionInfo与线程绑定，同时在TransactionInfo中由一个变量来保存以前的TransactionInfo，这样就持有了
		//一连串与事务处理相关的TransactionInfo
		//虽然不一定需要创建新的事务，但是总会在请求事务时创建TransactionInfo
		txInfo.bindToThread();
		return txInfo;
	}

	/**
	 * Execute after successful completion of call, but not after an exception was handled.
	 * Do nothing if we didn't create a transaction.
	 * 在成功完成调用后执行，但不在处理异常后执行。
	 * 如果我们没有创建事务，就什么都不做。
	 * @param txInfo information about the current transaction
	 */
	protected void commitTransactionAfterReturning(@Nullable TransactionInfo txInfo) {
		if (txInfo != null && txInfo.getTransactionStatus() != null) {
			if (logger.isTraceEnabled()) {
				logger.trace("Completing transaction for [" + txInfo.getJoinpointIdentification() + "]");
			}
			txInfo.getTransactionManager().commit(txInfo.getTransactionStatus());
		}
	}

	/**
	 * Handle a throwable, completing the transaction.
	 * We may commit or roll back, depending on the configuration.
	 *处理throwable，完成事务。
	 *我们可以提交或回滚，这取决于配置。
	 * @param txInfo information about the current transaction
	 * @param ex throwable encountered
	 */
	protected void completeTransactionAfterThrowing(@Nullable TransactionInfo txInfo, Throwable ex) {
		if (txInfo != null && txInfo.getTransactionStatus() != null) {
			if (logger.isTraceEnabled()) {
				logger.trace("Completing transaction for [" + txInfo.getJoinpointIdentification() +
						"] after exception: " + ex);
			}
			if (txInfo.transactionAttribute != null && txInfo.transactionAttribute.rollbackOn(ex)) {
				try {
					txInfo.getTransactionManager().rollback(txInfo.getTransactionStatus());
				}
				catch (TransactionSystemException ex2) {
					logger.error("Application exception overridden by rollback exception", ex);
					ex2.initApplicationException(ex);
					throw ex2;
				}
				catch (RuntimeException | Error ex2) {
					logger.error("Application exception overridden by rollback exception", ex);
					throw ex2;
				}
			}
			else {
				// We don't roll back on this exception.
				// Will still roll back if TransactionStatus.isRollbackOnly() is true.
				try {
					txInfo.getTransactionManager().commit(txInfo.getTransactionStatus());
				}
				catch (TransactionSystemException ex2) {
					logger.error("Application exception overridden by commit exception", ex);
					ex2.initApplicationException(ex);
					throw ex2;
				}
				catch (RuntimeException | Error ex2) {
					logger.error("Application exception overridden by commit exception", ex);
					throw ex2;
				}
			}
		}
	}

	/**
	 * Reset the TransactionInfo ThreadLocal.
	 * <p>Call this in all cases: exception or normal return!
	 * @param txInfo information about the current transaction (may be {@code null})
	 */
	protected void cleanupTransactionInfo(@Nullable TransactionInfo txInfo) {
		if (txInfo != null) {
			txInfo.restoreThreadLocalStatus();
		}
	}


	/**
	 * Opaque object used to hold transaction information. Subclasses
	 * must pass it back to methods on this class, but not see its internals.
	 */
	//事务信息DTO，用来传递事务数据
	protected static final class TransactionInfo {

		/**
		 * 事务管理器
		 */
		@Nullable
		private final PlatformTransactionManager transactionManager;

		/**
		 * 事务属性对象
		 */
		@Nullable
		private final TransactionAttribute transactionAttribute;

		/**
		 * 事务方法名
		 */
		private final String joinpointIdentification;

		/**
		 * 事务处理状态
		 */
		@Nullable
		private TransactionStatus transactionStatus;

		/**
		 * 旧的事务对象
		 */
		@Nullable
		private TransactionInfo oldTransactionInfo;

		public TransactionInfo(@Nullable PlatformTransactionManager transactionManager,
				@Nullable TransactionAttribute transactionAttribute, String joinpointIdentification) {

			this.transactionManager = transactionManager;
			this.transactionAttribute = transactionAttribute;
			this.joinpointIdentification = joinpointIdentification;
		}

		public PlatformTransactionManager getTransactionManager() {
			Assert.state(this.transactionManager != null, "No PlatformTransactionManager set");
			return this.transactionManager;
		}

		@Nullable
		public TransactionAttribute getTransactionAttribute() {
			return this.transactionAttribute;
		}

		/**
		 * Return a String representation of this joinpoint (usually a Method call)
		 * for use in logging.
		 */
		public String getJoinpointIdentification() {
			return this.joinpointIdentification;
		}

		public void newTransactionStatus(@Nullable TransactionStatus status) {
			this.transactionStatus = status;
		}

		@Nullable
		public TransactionStatus getTransactionStatus() {
			return this.transactionStatus;
		}

		/**
		 * Return whether a transaction was created by this aspect,
		 * or whether we just have a placeholder to keep ThreadLocal stack integrity.
		 */
		/**
		 *返回事务是否由此切面创建，
		 *或者我们是否只有一个占位符来保持线程本地堆栈的完整性。
		 */
		public boolean hasTransaction() {
			return (this.transactionStatus != null);
		}

		private void bindToThread() {
			// Expose current TransactionStatus, preserving any existing TransactionStatus
			// for restoration after this transaction is complete.
			//暴露当前TransactionStatus，保留任何现有TransactionStatus
            //用于此事务完成后的还原。
			this.oldTransactionInfo = transactionInfoHolder.get();
			transactionInfoHolder.set(this);
		}

		private void restoreThreadLocalStatus() {
			// Use stack to restore old transaction TransactionInfo.
			// Will be null if none was set.
			//使用堆栈还原旧事务处理信息。
            //如果未设置，则为空。
			transactionInfoHolder.set(this.oldTransactionInfo);
		}

		@Override
		public String toString() {
			return (this.transactionAttribute != null ? this.transactionAttribute.toString() : "No transaction");
		}
	}


	/**
	 * Simple callback interface for proceeding with the target invocation.
	 * Concrete interceptors/aspects adapt this to their invocation mechanism.
	 */
	@FunctionalInterface
	protected interface InvocationCallback {

		Object proceedWithInvocation() throws Throwable;
	}


	/**
	 * Internal holder class for a Throwable in a callback transaction model.
	 */
	private static class ThrowableHolder {

		@Nullable
		public Throwable throwable;
	}


	/**
	 * Internal holder class for a Throwable, used as a RuntimeException to be
	 * thrown from a TransactionCallback (and subsequently unwrapped again).
	 */
	@SuppressWarnings("serial")
	private static class ThrowableHolderException extends RuntimeException {

		public ThrowableHolderException(Throwable throwable) {
			super(throwable);
		}

		@Override
		public String toString() {
			return getCause().toString();
		}
	}


	/**
	 * Inner class to avoid a hard dependency on the Vavr library at runtime.
	 */
	private static class VavrDelegate {

		public static boolean isVavrTry(Object retVal) {
			return (retVal instanceof Try);
		}

		public static Object evaluateTryFailure(Object retVal, TransactionAttribute txAttr, TransactionStatus status) {
			return ((Try<?>) retVal).onFailure(ex -> {
				if (txAttr.rollbackOn(ex)) {
					status.setRollbackOnly();
				}
			});
		}
	}

	/**
	 * Inner class to avoid a hard dependency on Kotlin at runtime.
	 * 内部类，以避免在运行时对Kotlin产生硬依赖。
	 */
	private static class KotlinDelegate {

		static private boolean isSuspend(Method method) {
			KFunction<?> function = ReflectJvmMapping.getKotlinFunction(method);
			return function != null && function.isSuspend();
		}
	}


	/**
	 * Delegate for Reactor-based management of transactional methods with a
	 * reactive return type.
	 */
	private class ReactiveTransactionSupport {

		private final ReactiveAdapter adapter;

		public ReactiveTransactionSupport(ReactiveAdapter adapter) {
			this.adapter = adapter;
		}

		public Object invokeWithinTransaction(Method method, @Nullable Class<?> targetClass,
				InvocationCallback invocation, @Nullable TransactionAttribute txAttr, ReactiveTransactionManager rtm) {

			String joinpointIdentification = methodIdentification(method, targetClass, txAttr);

			// Optimize for Mono
			if (Mono.class.isAssignableFrom(method.getReturnType())) {
				return TransactionContextManager.currentContext().flatMap(context ->
						createTransactionIfNecessary(rtm, txAttr, joinpointIdentification).flatMap(it -> {
							try {
								// Need re-wrapping until we get hold of the exception through usingWhen.
								return Mono.<Object, ReactiveTransactionInfo>usingWhen(
										Mono.just(it),
										txInfo -> {
											try {
												return (Mono<?>) invocation.proceedWithInvocation();
											}
											catch (Throwable ex) {
												return Mono.error(ex);
											}
										},
										this::commitTransactionAfterReturning,
										(txInfo, err) -> Mono.empty(),
										this::commitTransactionAfterReturning)
										.onErrorResume(ex ->
												completeTransactionAfterThrowing(it, ex).then(Mono.error(ex)));
							}
							catch (Throwable ex) {
								// target invocation exception
								return completeTransactionAfterThrowing(it, ex).then(Mono.error(ex));
							}
						})).subscriberContext(TransactionContextManager.getOrCreateContext())
						.subscriberContext(TransactionContextManager.getOrCreateContextHolder());
			}

			// Any other reactive type, typically a Flux
			return this.adapter.fromPublisher(TransactionContextManager.currentContext().flatMapMany(context ->
					createTransactionIfNecessary(rtm, txAttr, joinpointIdentification).flatMapMany(it -> {
						try {
							// Need re-wrapping until we get hold of the exception through usingWhen.
							return Flux
									.usingWhen(
											Mono.just(it),
											txInfo -> {
												try {
													return this.adapter.toPublisher(invocation.proceedWithInvocation());
												}
												catch (Throwable ex) {
													return Mono.error(ex);
												}
											},
											this::commitTransactionAfterReturning,
											(txInfo, ex) -> Mono.empty(),
											this::commitTransactionAfterReturning)
									.onErrorResume(ex ->
											completeTransactionAfterThrowing(it, ex).then(Mono.error(ex)));
						}
						catch (Throwable ex) {
							// target invocation exception
							return completeTransactionAfterThrowing(it, ex).then(Mono.error(ex));
						}
					})).subscriberContext(TransactionContextManager.getOrCreateContext())
					.subscriberContext(TransactionContextManager.getOrCreateContextHolder()));
		}

		@SuppressWarnings("serial")
		private Mono<ReactiveTransactionInfo> createTransactionIfNecessary(ReactiveTransactionManager tm,
				@Nullable TransactionAttribute txAttr, final String joinpointIdentification) {

			// If no name specified, apply method identification as transaction name.
			if (txAttr != null && txAttr.getName() == null) {
				txAttr = new DelegatingTransactionAttribute(txAttr) {
					@Override
					public String getName() {
						return joinpointIdentification;
					}
				};
			}

			final TransactionAttribute attrToUse = txAttr;
			Mono<ReactiveTransaction> tx = (attrToUse != null ? tm.getReactiveTransaction(attrToUse) : Mono.empty());
			return tx.map(it -> prepareTransactionInfo(tm, attrToUse, joinpointIdentification, it)).switchIfEmpty(
					Mono.defer(() -> Mono.just(prepareTransactionInfo(tm, attrToUse, joinpointIdentification, null))));
		}

		private ReactiveTransactionInfo prepareTransactionInfo(@Nullable ReactiveTransactionManager tm,
				@Nullable TransactionAttribute txAttr, String joinpointIdentification,
				@Nullable ReactiveTransaction transaction) {

			ReactiveTransactionInfo txInfo = new ReactiveTransactionInfo(tm, txAttr, joinpointIdentification);
			if (txAttr != null) {
				// We need a transaction for this method...
				if (logger.isTraceEnabled()) {
					logger.trace("Getting transaction for [" + txInfo.getJoinpointIdentification() + "]");
				}
				// The transaction manager will flag an error if an incompatible tx already exists.
				txInfo.newReactiveTransaction(transaction);
			}
			else {
				// The TransactionInfo.hasTransaction() method will return false. We created it only
				// to preserve the integrity of the ThreadLocal stack maintained in this class.
				if (logger.isTraceEnabled()) {
					logger.trace("Don't need to create transaction for [" + joinpointIdentification +
							"]: This method isn't transactional.");
				}
			}

			return txInfo;
		}

		private Mono<Void> commitTransactionAfterReturning(@Nullable ReactiveTransactionInfo txInfo) {
			if (txInfo != null && txInfo.getReactiveTransaction() != null) {
				if (logger.isTraceEnabled()) {
					logger.trace("Completing transaction for [" + txInfo.getJoinpointIdentification() + "]");
				}
				return txInfo.getTransactionManager().commit(txInfo.getReactiveTransaction());
			}
			return Mono.empty();
		}

		private Mono<Void> completeTransactionAfterThrowing(@Nullable ReactiveTransactionInfo txInfo, Throwable ex) {
			if (txInfo != null && txInfo.getReactiveTransaction() != null) {
				if (logger.isTraceEnabled()) {
					logger.trace("Completing transaction for [" + txInfo.getJoinpointIdentification() +
							"] after exception: " + ex);
				}
				if (txInfo.transactionAttribute != null && txInfo.transactionAttribute.rollbackOn(ex)) {
					return txInfo.getTransactionManager().rollback(txInfo.getReactiveTransaction()).onErrorMap(ex2 -> {
								logger.error("Application exception overridden by rollback exception", ex);
								if (ex2 instanceof TransactionSystemException) {
									((TransactionSystemException) ex2).initApplicationException(ex);
								}
								return ex2;
							}
					);
				}
				else {
					// We don't roll back on this exception.
					// Will still roll back if TransactionStatus.isRollbackOnly() is true.
					return txInfo.getTransactionManager().commit(txInfo.getReactiveTransaction()).onErrorMap(ex2 -> {
								logger.error("Application exception overridden by commit exception", ex);
								if (ex2 instanceof TransactionSystemException) {
									((TransactionSystemException) ex2).initApplicationException(ex);
								}
								return ex2;
							}
					);
				}
			}
			return Mono.empty();
		}
	}


	/**
	 * Opaque object used to hold transaction information for reactive methods.
	 * 用于保存被动方法的事务信息的不透明对象。
	 */
	private static final class ReactiveTransactionInfo {

		@Nullable
		private final ReactiveTransactionManager transactionManager;

		@Nullable
		private final TransactionAttribute transactionAttribute;

		private final String joinpointIdentification;

		@Nullable
		private ReactiveTransaction reactiveTransaction;

		public ReactiveTransactionInfo(@Nullable ReactiveTransactionManager transactionManager,
				@Nullable TransactionAttribute transactionAttribute, String joinpointIdentification) {

			this.transactionManager = transactionManager;
			this.transactionAttribute = transactionAttribute;
			this.joinpointIdentification = joinpointIdentification;
		}

		public ReactiveTransactionManager getTransactionManager() {
			Assert.state(this.transactionManager != null, "No ReactiveTransactionManager set");
			return this.transactionManager;
		}

		@Nullable
		public TransactionAttribute getTransactionAttribute() {
			return this.transactionAttribute;
		}

		/**
		 * Return a String representation of this joinpoint (usually a Method call)
		 * for use in logging.
		 */
		public String getJoinpointIdentification() {
			return this.joinpointIdentification;
		}

		public void newReactiveTransaction(@Nullable ReactiveTransaction transaction) {
			this.reactiveTransaction = transaction;
		}

		@Nullable
		public ReactiveTransaction getReactiveTransaction() {
			return this.reactiveTransaction;
		}

		@Override
		public String toString() {
			return (this.transactionAttribute != null ? this.transactionAttribute.toString() : "No transaction");
		}
	}

}
