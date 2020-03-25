package com.ivo.common.utils.excel;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ExcelParam implements Serializable{

	private static final long serialVersionUID = -4231868339831975335L;
	//文件地址,本地读取时用
	private String filePath;
	//反射具体类
	private Class clazz;
	//从第几行开始扫描
	private Integer rowNumIndex;
	//读取到第几个sheet结束
	private Integer sheetIndex;
	//读取指定的sheetName或写入指定的sheetName
	private String sheetName;
	//存储属性和表头的对应关系
	private Map map;
	//keyValue
	private String keyValue;
	//表头是否强一致
	private Boolean sameHeader = false;
	//是否流读取
	private Boolean stream = false;
	//用流代替本地文件
	private byte[] buf;
	//输出流
	private HttpServletResponse response;
	//文件名
	private String fileName;
	//文件输出路径
	private String outFilePath;
	//文件导出封装数据
	private List list;

	public ExcelParam() {
	}

	public ExcelParam(Class clazz, String keyValue, String outFilePath, List list) {
		this.clazz = clazz;
		this.keyValue = keyValue;
		this.outFilePath = outFilePath;
		this.list = list;
	}

	public ExcelParam(Class clazz, String outFilePath, List list) {
		this.clazz = clazz;
		this.outFilePath = outFilePath;
		this.list = list;
	}
	public ExcelParam(Class clazz, HttpServletResponse response,List list) {
		this.clazz = clazz;
		this.response = response;
		this.list = list;
	}

	public ExcelParam(Class clazz, String keyValue, HttpServletResponse response,List list) {
		this.clazz = clazz;
		this.keyValue = keyValue;
		this.response = response;
		this.list = list;
	}

	public ExcelParam(Class clazz, String keyValue, HttpServletResponse response, String fileName,List list) {
		this.clazz = clazz;
		this.keyValue = keyValue;
		this.response = response;
		this.fileName = fileName;
		this.list = list;
	}

	public ExcelParam(Class clazz, HttpServletResponse response, String fileName,List list) {
		this.clazz = clazz;
		this.response = response;
		this.fileName = fileName;
		this.list = list;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

	public Integer getRowNumIndex() {
		return rowNumIndex;
	}

	public void setRowNumIndex(Integer rowNumIndex) {
		this.rowNumIndex = rowNumIndex;
	}

	public Integer getSheetIndex() {
		return sheetIndex;
	}

	public void setSheetIndex(Integer sheetIndex) {
		this.sheetIndex = sheetIndex;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public Boolean getSameHeader() {
		return sameHeader;
	}

	public void setSameHeader(Boolean sameHeader) {
		this.sameHeader = sameHeader;
	}

	public Boolean getStream() {
		return stream;
	}

	public void setStream(Boolean stream) {
		this.stream = stream;
	}

	public byte[] getBuf() {
		return buf;
	}

	public void setBuf(byte[] buf) {
		this.buf = buf;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getOutFilePath() {
		return outFilePath;
	}

	public void setOutFilePath(String outFilePath) {
		this.outFilePath = outFilePath;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
}
