package testhelper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


public class TestActionResultJsonHelper {
	
	private JSONObject jsonObject;
	
	public void setResult(String result) {
		jsonObject = JSON.parseObject(result);
	}

	public String getResultInfo() {
		return jsonObject.getString("resultInfo");
	}

	public Integer getResultCode() {
		return jsonObject.getInteger("serviceResult");
	}
	
	public boolean isSuccess() {
		return jsonObject.getBoolean("isSuccess");
	}

	public String getResultParm() {
		return jsonObject.getString("resultParm");
	}

	public void print() {
		System.out.println("resultInfo:\t" + getResultInfo());
		System.out.println("serviceResult:\t" + getResultCode());
		System.out.println("resultParm:\t" + getResultParm());
	}
	
	@Override
	public String toString() {
		return "resultInfo:\t" + getResultInfo() + "\n" +
				"serviceResult:\t" + getResultCode() + "\n" +
				"resultParm:\t" + getResultParm();
	}
}
