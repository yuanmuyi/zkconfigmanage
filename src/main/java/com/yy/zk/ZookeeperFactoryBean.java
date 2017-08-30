package com.yy.zk;

import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.UnhandledErrorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**     
 * @Description:zk连接工厂bean  
 * @author yang.yuan    
 * @date 2017年8月30日 上午10:13:23  
 * @reviewer  
 */
public class ZookeeperFactoryBean implements FactoryBean<CuratorFramework>,InitializingBean,DisposableBean{
	
//	private Logger logger = LoggerFactory.getLogger(This.class);
	private CuratorFramework zkClient;
	
	private List<IZKListener> listeners;
	
	private String zkConnectionString;
	
	public void setListeners(List<IZKListener> listeners) {
		this.listeners = listeners;
	}

	public void setZkConnectionString(String zkConnectionString) {
		this.zkConnectionString = zkConnectionString;
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		zkClient = createWithOptions(zkConnectionString, retryPolicy, 2000, 10000);
		registerListeners(zkClient);
		zkClient.start();
	}

	@Override
	public CuratorFramework getObject() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 通过自定义参数创建  
	 * @author yang.yuan
	 * @data 2017年8月30日
	 * @param @param connectionString
	 * @param @param retryPolicy
	 * @param @param connectionTimeoutMs
	 * @param @param seeionTimeoutMs
	 * @param @return
	 */
	public CuratorFramework createWithOptions(String connectionString,RetryPolicy retryPolicy,int connectionTimeoutMs,int seeionTimeoutMs){
		return CuratorFrameworkFactory.builder()
				.connectString(connectionString)
				.retryPolicy(retryPolicy)
				.connectionTimeoutMs(connectionTimeoutMs)
				.sessionTimeoutMs(seeionTimeoutMs)
				.build();
	}
	
	//注册需要监听的监听者对象
	private void registerListeners(final CuratorFramework client){
		client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
			
			@Override
			public void stateChanged(CuratorFramework arg0, ConnectionState newState) {
//				logger.info("CuratorFramework state changed: {}",newState);
				if(newState == ConnectionState.CONNECTED || newState == ConnectionState.CONNECTED.RECONNECTED){
					for(IZKListener listener : listeners){
						listener.executor(client);
//						logger.info("Listener {} executed",listener.getClass().getName());
					}
				}
			}
		});
		client.getUnhandledErrorListenable().addListener(new UnhandledErrorListener() {
			
			@Override
			public void unhandledError(String arg0, Throwable arg1) {
//				logger.info("CuratorFramework unhandledError: {}", arg0);
			}
		});
	}
}
