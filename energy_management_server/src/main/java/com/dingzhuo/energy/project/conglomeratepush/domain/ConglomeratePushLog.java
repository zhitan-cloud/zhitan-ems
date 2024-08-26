
package com.dingzhuo.energy.project.conglomeratepush.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 集团数据推送实体类
 *
 * @author zhw
 * @since 2022-03-09
 */
public class ConglomeratePushLog implements Serializable {

	private static final long serialVersionUID = 1L;


	/**
	 * 主键
	 */
		private String id;

		/**
		* 推送状态（Y、N）
		*/
		private String pushStatus;
		/**
		* 推送时间
		*/
		private Date pushTime;
		/**
		* 推送内容（json串）
		*/
		private String pushContent;
		/**
		* 推送次数
		*/
		private Long pushCount;

		/**
		* 发送返回信息(sendRes)
		*/
		private String pushResult;

		/**
		 * 异常信息记录
		 */
		private String errorInfo;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPushStatus() {
		return pushStatus;
	}

	public void setPushStatus(String pushStatus) {
		this.pushStatus = pushStatus;
	}

	public Date getPushTime() {
		return pushTime;
	}

	public void setPushTime(Date pushTime) {
		this.pushTime = pushTime;
	}

	public String getPushContent() {
		return pushContent;
	}

	public void setPushContent(String pushContent) {
		this.pushContent = pushContent;
	}

	public Long getPushCount() {
		return pushCount;
	}

	public void setPushCount(Long pushCount) {
		this.pushCount = pushCount;
	}

	public String getPushResult() {
		return pushResult;
	}

	public void setPushResult(String pushResult) {
		this.pushResult = pushResult;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
}
