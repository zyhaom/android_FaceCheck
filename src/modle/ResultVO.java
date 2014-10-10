package modle;

/**
 * 
 * 人脸识别的结果Bean
 *
 */
public class ResultVO {
	private String picUrl = "";// 图片url
	private String semblance = "";// 相似度
	private String name = "";// 姓名
	private String addr = "";// 地址
	private String linkman = "";// 联系人
	private String tel = "";// 联系电话

	public ResultVO() {}
	
	public ResultVO(String picUrl, String sameTime, String name, String addr, String linkman, String tel) {
		this.picUrl = picUrl;
		this.semblance = sameTime;
		this.name = name;
		this.addr = addr;
		this.linkman = linkman;
		this.tel = tel;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getSameTime() {
		return semblance;
	}

	public void setSameTime(String sameTime) {
		this.semblance = sameTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Override
	public String toString() {
		return "ResultVO [picUrl=" + picUrl + ", sameTime=" + semblance + ", name=" + name + ", addr=" + addr + ", linkman=" + linkman + ", tel=" + tel + "]";
	}
}
