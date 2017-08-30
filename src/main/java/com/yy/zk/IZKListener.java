package com.yy.zk;

import org.apache.curator.framework.CuratorFramework;

/**     
 * @Description:所有需要在ZK客户端链接成功后需要做的事件，需要实现这个接口，由上面的ZookeeperFactoryBean统一调度  
 * @author yang.yuan    
 * @date 2017年8月30日 上午11:29:48  
 * @reviewer  
 */
public interface IZKListener {
	void executor(CuratorFramework client);
}
