package org.jbpm.enterprise.platform;

/**
 * Factory responsible for providing instances of <code>ExecutionEngine</code> that provides access to bpm world.
 * 
 * Automatic resource resolving mechanism, this is internal simplification for developers to get most of the platform with
 * as less coding as possible. In general it does following:
 * <ul>
 * 	<li>locates executionengine-spec.xml</li>
 * 	<li>locates humantask-spec.xml</li>
 * 	<li>creates internally <code>ExecutionEngineConfiguration</code> instance with information from two spec files</li>
 * 	<li>builds default <code>SessionMappingStrategy</code> instance</li>
 * 	<li>registers default <code>ExecutionEngineCallback</code> instance to be able to inject session into work item handlers that requires them</li>
 * </ul>
 *
 */
public interface ExecutionEngineFactory {

	/**
	 * Default factory method that relies on automatic resource resolving mechanism. 
	 * @param bundleClassLoader OSGi bundle class loader
	 * @return ready to use instance of <code>ExecutionEngine</code>
	 * @throws IllegalArgumentException when <code>bundleClassLoader</code> is null
	 */
	public ExecutionEngine newExecutionEngine(ClassLoader bundleClassLoader);

	/**
	 * Builds <code>ExecutionEngine</code> based on supplied configuration, which disables automatic resource resolving mechanism. 
	 * @param bundleClassLoader OSGi bundle class loader
	 * @param config user defined <code>ExecutionEngineConfiguration</code>
	 * @return ready to use instance of <code>ExecutionEngine</code>
	 * @throws IllegalArgumentException when <code>bundleClassLoader</code> is null
	 */
	public ExecutionEngine newExecutionEngine(ClassLoader bundleClassLoader, ExecutionEngineConfiguration config);

	/**
	 * Builds <code>ExecutionEngine</code> based on supplied configuration, which disables automatic resource resolving mechanism
	 * and instructs the engine to use provided <code>SessionMappingStrategy</code> instead of the default one.
	 * @param bundleClassLoader OSGi bundle class loader
	 * @param config user defined <code>ExecutionEngineConfiguration</code>
	 * @param callback user defined callback to receive event notifications
	 * @return ready to use instance of <code>ExecutionEngine</code>
	 * @throws IllegalArgumentException when <code>bundleClassLoader</code> is null
	 */
	public ExecutionEngine newExecutionEngine(ClassLoader bundleClassLoader, ExecutionEngineConfiguration config, Object callback);

	/**
	 * Builds <code>ExecutionEngine</code> based on supplied configuration, which disables automatic resource resolving mechanism
	 * and instructs the engine to use provided <code>SessionMappingStrategy</code> instead of the default one. With provided callback instance caller
	 * will be notified whenever important events happen in the engine.
	 * This is the most flexible option for building <code>ExecutionEngine</code> instances.
	 * @param bundleClassLoader bundleClassLoader OSGi bundle class loader
	 * @param config config user defined <code>ExecutionEngineConfiguration</code>
	 * @param strategy user defined <code>SessionMappingStrategy</code>
	 * @param callback user defined callback to receive event notifications
	 * @return ready to use instance of <code>ExecutionEngine</code>
	 * @throws IllegalArgumentException when <code>bundleClassLoader</code> is null
	 */
	public ExecutionEngine newExecutionEngine(ClassLoader bundleClassLoader, ExecutionEngineConfiguration config, SessionMappingStrategy strategy,
			Object callback);

}
