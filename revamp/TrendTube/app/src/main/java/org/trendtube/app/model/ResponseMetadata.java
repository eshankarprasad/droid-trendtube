package org.trendtube.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseMetadata implements Serializable {
    private static final long serialVersionUID = 1470333138304653432L;

    @SerializedName ("metainfo")
    public MetaInfo metaInfo;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    public class MetaInfo implements Serializable {


        @SerializedName ("statusCode")
        public String responseStatusCode = "";

        @SerializedName ("statusMessage")
        public String responseMessage = "";

        public String getResponseStatusCode() {
            return responseStatusCode;
        }

        public void setResponseStatusCode(String responseStatusCode) {
            this.responseStatusCode = responseStatusCode;
        }

        public String getResponseMessage() {
            return responseMessage;
        }

        public void setResponseMessage(String responseMessage) {
            this.responseMessage = responseMessage;
        }


        @Override
        public String toString() {
            return "ResponseMetadata{" +
                    "responseStatusCode='" + responseStatusCode + '\'' +
                    ", responseMessage='" + responseMessage + '\'' +
                    '}';
        }

    }
}






/*import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseMetadata implements Serializable {
	private static final long serialVersionUID = 1470333138304653432L;

	@SerializedName("code")
	private String responseCode;

	@SerializedName("msg")
	private String responseMessage;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @Override
    public String toString() {
        return "ResponseMetadata{" +
                "responseCode='" + responseCode + '\'' +
                ", responseMessage='" + responseMessage + '\'' +
                '}';
    }

    //	@SerializedName("code")
//	public String responseStatusCode;
//
//	@SerializedName("msg")
//	public String responseMessage;


//	public int getResponseStatusCodeInt() {
//		try {
//			return Integer.parseInt(responseStatusCode);
//		} catch (Exception e) {
//		}
//		return -1;
//	}

}*/
