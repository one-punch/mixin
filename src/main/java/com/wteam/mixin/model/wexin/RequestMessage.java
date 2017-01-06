package com.wteam.mixin.model.wexin;

/**
 * <p>Title:微信服务器的请求数据实体类</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年8月6日
 */
public final class RequestMessage {
	
	/**ToUserName	开发者微信号*/
	private String ToUserName;
	
	/**FromUserName	发送方帐号（一个OpenID）*/
	private String FromUserName;
	
	/**CreateTime	消息创建时间 （整型）*/
	private long CreateTime;
	
	/**MsgType	消息类型*/
	private String MsgType;
	
	/**MsgId	消息id，64位整型,该字段除了event类型消息外，其他的MsgType均包含*/
	private long MsgId;
	
	/*
	 * 文字消息额外参数
	 */
	/**Content	文本消息内容*/
	private String Content;
	
	/*
	 * 链接消息额外参数
	 */
	/**Title	消息标题*/
	private String Title;
	
	/**Description	消息描述*/
	private String Description;
	
	/**Url	消息链接*/
	private String Url;
	
	/*
	 * 地理位置消息额外参数
	 */
	/**Location_X	地理位置维度*/
	private String Location_X;
	
	/**Location_Y	地理位置经度*/
	private String Location_Y;
	
	/**Scale	地图缩放大小*/
	private String Scale;
	
	/**Label	地理位置信息*/
	private String Label;
	
	/*
	 * 图片消息额外参数
	 */
	/**MediaId 多媒体Id， 多媒体消息（图片，视频）均包含该字段
	 * 	图片消息媒体id，可以调用多媒体文件下载接口拉取数据
	 * 	视频消息媒体id，可以调用多媒体文件下载接口拉取数据。
	 * 	
	 */
	private String MediaId;
	
	/**PicUrl	图片链接（由系统生成）*/
	private String PicUrl;

	/*
	 * 视频消息额外参数
	 */
	/**ThumbMediaId	视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。*/
	private String ThumbMediaId;
	
	/*
	 * 声音消息额外参数
	 */
	/**Format	语音格式，如amr，speex等*/
	private String Format;
	
	private String Recognition;
	
	/*
	 * Event消息额外参数
	 */
	/**Event	事件类型，CLICK or VIEW or LOCATION*/
	private String Event;
	
	/**EventKey	事件KEY值，与自定义菜单接口中KEY值对应 or 设置的跳转URL*/
	private String EventKey;
	
	/**LOCATION事件额外参数、Latitude	地理位置纬度*/
	private String Latitude;
	
	/**LOCATION事件额外参数、Longitude	地理位置经度*/
	private String Longitude;
	
	/**LOCATION事件额外参数、Precision	地理位置精度*/
	private String Precision;
	
	/**SCAN事件额外参数、二维码的ticket，可用来换取二维码图片*/
	private String Tickey;
	
	/**VIEW事件额外参数*/
	private String MenuId;

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public long getMsgId() {
		return MsgId;
	}

	public void setMsgId(long msgId) {
		MsgId = msgId;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getLocation_X() {
		return Location_X;
	}

	public void setLocation_X(String location_X) {
		Location_X = location_X;
	}

	public String getLocation_Y() {
		return Location_Y;
	}

	public void setLocation_Y(String location_Y) {
		Location_Y = location_Y;
	}

	public String getScale() {
		return Scale;
	}

	public void setScale(String scale) {
		Scale = scale;
	}

	public String getLabel() {
		return Label;
	}

	public void setLabel(String label) {
		Label = label;
	}

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

	public String getThumbMediaId() {
		return ThumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		ThumbMediaId = thumbMediaId;
	}

	public String getFormat() {
		return Format;
	}

	public void setFormat(String format) {
		Format = format;
	}

	public String getRecognition() {
		return Recognition;
	}

	public void setRecognition(String recognition) {
		Recognition = recognition;
	}
	
	public String getEvent() {
		return Event;
	}

	public void setEvent(String event) {
		Event = event;
	}

	public String getEventKey() {
		return EventKey;
	}

	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String latitude) {
		Latitude = latitude;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	public String getPrecision() {
		return Precision;
	}

	public void setPrecision(String precision) {
		Precision = precision;
	}

	public String getTickey() {
		return Tickey;
	}

	public void setTickey(String tickey) {
		Tickey = tickey;
	}

	public String getMenuId() {
		return MenuId;
	}

	public void setMenuId(String menuId) {
		MenuId = menuId;
	}

	@Override
	public String toString() {
		return "Msg [ToUserName=" + ToUserName + ", FromUserName=" + FromUserName + ", CreateTime=" + CreateTime
				+ ", MsgType=" + MsgType + ", MsgId=" + MsgId + ", Content=" + Content + ", Title=" + Title
				+ ", Description=" + Description + ", Url=" + Url + ", Location_X=" + Location_X + ", Location_Y="
				+ Location_Y + ", Scale=" + Scale + ", Label=" + Label + ", MediaId=" + MediaId + ", PicUrl=" + PicUrl
				+ ", ThumbMediaId=" + ThumbMediaId + ", Format=" + Format + ", Recognition=" + Recognition + "]";
	}
}















