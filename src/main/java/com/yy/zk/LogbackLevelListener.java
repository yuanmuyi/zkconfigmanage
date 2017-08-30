package com.yy.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

/**     
 * @Description:  
 * @author yang.yuan    
 * @date 2017年8月30日 下午2:13:14  
 * @reviewer  
 */
public class LogbackLevelListener implements IZKListener {

//	Logger logger = (Logger)LoggerFactory.getLogger(This.class);
	
	private String path;
	
	
	public LogbackLevelListener(String path) {
		this.path = path;
	}



	@Override
	public void executor(CuratorFramework client) {
		final NodeCache nodeCache = new NodeCache(client, path);
		nodeCache.getListenable().addListener(new NodeCacheListener() {
			
			@Override
			public void nodeChanged() throws Exception {
				byte [] data = nodeCache.getCurrentData().getData();
				if(data != null){
					String level = new String(data);
					Logger log = (Logger) LoggerFactory.getLogger("root");
                    Level newLevel = Level.fromLocationAwareLoggerInteger(Integer.parseInt(level));
                    log.setLevel(newLevel);
                    System.out.println("Setting logback new level to :" + newLevel.levelStr);
				}
			}
		});
		 try {
			 nodeCache.start(true);
	     } catch (Exception e) {
//	        	logger.error("Start NodeCache error for path: {}, error info: {}", path, e.getMessage());
	     }
	}

}
