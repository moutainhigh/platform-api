package com.xinleju.platform.finance.utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DebugInfo implements Serializable{


    /**
     * @Fields serialVersionUID : TODO(目的和意义)
     */
    private static final long serialVersionUID = 6266291649520654801L;
    private String startAt;
    private String completeAt;
    private LinkedList<String> funcDesc;
    private LinkedList<String> errDesc;
    
    public DebugInfo(){
        
        this.startAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        this.funcDesc = new LinkedList<String>();
        this.errDesc = new LinkedList<String>();
    }
    
    public void addErrDesc(String m){
        this.errDesc.add(m);
    }
    
    public void addFuncDesc(String m){
        this.funcDesc.add(m);
    }
    
    public String getStartAt() {
        return startAt;
    }
    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }
    public String getCompleteAt() {
        return completeAt;
    }
    public void setCompleteAt(String completeAt) {
        this.completeAt = completeAt;
    }
    public LinkedList<String> getFuncDesc() {
        return funcDesc;
    }
    public void setFuncDesc(LinkedList<String> funcDesc) {
        this.funcDesc = funcDesc;
    }
    public LinkedList<String> getErrDesc() {
        return errDesc;
    }
    public void setErrDesc(LinkedList<String> errDesc) {
        this.errDesc = errDesc;
    }
    
   
}
