package service;

import java.util.Map;

public interface RegistService {

	boolean findUserName(Map<String, Object> conditions);

	boolean regist(Map<String, Object> conditions);

}
