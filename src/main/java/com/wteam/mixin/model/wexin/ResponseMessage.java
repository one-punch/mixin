package com.wteam.mixin.model.wexin;

import java.util.Arrays;

/**
 * 服务器回复内容
 * 
 * @author vee
 */
public final class ResponseMessage{
	
	/**ToUserName	开发者微信号*/
	private String ToUserName;
	
	/**FromUserName	发送方帐号（一个OpenID）*/
	private String FromUserName;
	
	/**CreateTime	消息创建时间 （整型）*/
	private long CreateTime;
	
	/**MsgType	消息类型*/
	private String MsgType;
	
	/*
	 * 文本消息回复 所需参数
	 */
	/**Content	文本消息内容*/
	private String Content;
	

	/*
	 * 图片消息回复 所需参数
	 */
	/**MediaId	通过素材管理中的接口上传多媒体文件，得到的id。*/
	private String MediaId;
	
	/*
	 * 图文消息回复 所需参数
	 */
	/**ArticleCount	必须	图文消息个数，限制为10条以内*/
	private String ArticleCount;
	
	/**Articles	是	多条图文消息信息，默认第一个item为大图,注意，如果图文数超过10，则将会无响应*/
	private String[] Articles;
	
	/**Title	否	图文消息标题、注意音乐消息也包含该字段*/
	private String Title;
	
	/**Description	否	图文消息描述、注意音乐消息也包含该字段*/
	private String Description;
	
	/**PicUrl	否	图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200*/
	private String PciUrl;
	
	/**Url	否	点击图文消息跳转链接*/
	private String Url;
	
	/*
	 * 音乐消息回复 所需参数  见上述 图文消息 模块变量定义
		 * Title 否	音乐标题、
		 * Description 否	音乐描述
	 */
	
	/**MusicURL	否	音乐链接*/
	private String MusicURL;
	
	/**HQMusicUrl	否	高质量音乐链接，WIFI环境优先使用该链接播放音乐*/
	private String HQMusicUrl;
	
	/**ThumbMediaId	是	缩略图的媒体id，通过素材管理中的接口上传多媒体文件，得到的id*/
	private String ThumbMediaId;

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

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(String articleCount) {
		ArticleCount = articleCount;
	}

	public String[] getArticles() {
		return Articles;
	}

	public void setArticles(String[] articles) {
		Articles = articles;
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

	public String getPciUrl() {
		return PciUrl;
	}

	public void setPciUrl(String pciUrl) {
		PciUrl = pciUrl;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getMusicURL() {
		return MusicURL;
	}

	public void setMusicURL(String musicURL) {
		MusicURL = musicURL;
	}

	public String getHQMusicUrl() {
		return HQMusicUrl;
	}

	public void setHQMusicUrl(String hQMusicUrl) {
		HQMusicUrl = hQMusicUrl;
	}

	public String getThumbMediaId() {
		return ThumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		ThumbMediaId = thumbMediaId;
	}

	@Override
	public String toString() {
		return "ResponseMessage [ToUserName=" + ToUserName + ", FromUserName=" + FromUserName + ", CreateTime="
				+ CreateTime + ", MsgType=" + MsgType + ", Content=" + Content + ", MediaId=" + MediaId
				+ ", ArticleCount=" + ArticleCount + ", Articles=" + Arrays.toString(Articles) + ", Title=" + Title
				+ ", Description=" + Description + ", PciUrl=" + PciUrl + ", Url=" + Url + ", MusicURL=" + MusicURL
				+ ", HQMusicUrl=" + HQMusicUrl + ", ThumbMediaId=" + ThumbMediaId + "]";
	}
	
	/**MediaId	必须 通过素材管理中的接口上传多媒体文件，得到的id*/

	/*
	 * 视频消息回复 所需参数 ：见上述 图文消息 模块变量定义
	 	 * MediaId 必须 通过素材管理中的接口上传多媒体文件，得到的id	
		 * Title 非必须 	视频消息的标题、
		 * Description 非必须	视频消息的描述
	 */
}
