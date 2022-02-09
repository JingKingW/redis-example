package com.xunmall.example.redis.sentinel;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * @author Wang Yunfei 2016年4月7日 下午4:12:11
 */
public class RedisSentinelHelper {
    
    private static Logger log = Logger.getLogger("redis.error");
    
    public static Set<String> getMasterNames(String pwd , Set<String> sentinels) {
    	 
        if(sentinels == null || sentinels.isEmpty()) {
            return Collections.emptySet();
        }
        
        List<String> masterNamesList =  new ArrayList<String>();
        Jedis sentinel = null;
        
        Iterator<String> it = sentinels.iterator();
        String shap = null;
        HostAndPort hap = null;
        while(it.hasNext()) {
            shap = it.next();
            try {
                hap = toHostAndPort(Arrays.asList(shap.split(":")));
                sentinel = new Jedis(hap.getHost(), hap.getPort());
                if(StringUtils.isNoneBlank(pwd)) {
                	String result = sentinel.auth(pwd);
                    log.info("sentinel auth result ="+result);
                }
                String sInfo = sentinel.info();
                
                if(StringUtils.isNotBlank(sInfo)) {
                    InputStream is = new ByteArrayInputStream(sInfo.getBytes());
                    Properties p = new Properties();
                    p.load(is);
                    
                    // check master number
                    int masterN = Integer.parseInt(p.getProperty("sentinel_masters"));
                    if(masterN > 0) {
                        String val = null;
                        
                        for (int i = 0; i < masterN; i++) {
                            val = p.getProperty("master" + i);
                            log.info(val);
                            String[] kv = val.split(",");
                            
                            // Master status=ok
                            if(kv[1].split("=")[1].equals("ok")) {
                                String masterName =kv[0].split("=")[1];
                                if(!masterNamesList.contains(masterName)) {
                                    masterNamesList.add(masterName);
                                }
                            }
                        }
                    }
                } else {
                    log.error("Sentinel command execute fail. host :" + hap.getHost() + " port: " + hap.getPort());
                }
            } catch (JedisConnectionException e) {
                log.error("Sentinel connect fail. host :" + hap.getHost() + " port: " + hap.getPort());
                it.remove();
            } catch (Exception e) {
                log.error("Sentinel fail. host :" + hap.getHost() + " port: " + hap.getPort());
            } finally {
                sentinel.close();
            }
        }
        
        if(masterNamesList.isEmpty()) {
            return Collections.emptySet();
        }
        
        Collections.sort(masterNamesList);
        Set<String> masterNamesSet = new LinkedHashSet<String>();
        for (String masterName : masterNamesList) {
            masterNamesSet.add(masterName);
        }
        
        return masterNamesSet;
    }
    
    public static Set<String> getMasterNames(Set<String> sentinels) {
    	return getMasterNames(null,sentinels);
    }
    
    private static HostAndPort toHostAndPort(List<String> hostAndPort){
        return new HostAndPort(hostAndPort.get(0), Integer.parseInt(hostAndPort.get(1)));
    }
}
