package com.whitelabel.app.dao;

import android.os.Handler;

import com.android.volley.VolleyError;
import com.whitelabel.app.model.ErrorMsgBean;
import com.whitelabel.app.model.SVRAppserviceCmsCmsPageReturnEntity;
import com.whitelabel.app.network.BaseHttp;
import com.whitelabel.app.utils.JJsonUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.LanguageUtils;
import com.whitelabel.app.utils.StoreUtils;

import java.util.TreeMap;

/**
 * Created by imaginato on 2016/3/18.
 */
public class HelpCenterDao extends BaseHttp {
    public static final int REQUEST_GETDATA = 1;
    public static final int REQUEST_GET_CUSTOMER_CARE = 2;
    public static final int REQUEST_ERROR = 10;

    public HelpCenterDao(String TAG, Handler handler) {
        super(TAG, handler);
    }

    public void loadDataFromServer(String url_key) {
        params = new TreeMap<>();
        params.put("url_key", url_key);
        requestHttp(HTTP_METHOD.GET, "appservice/cms/cmsPage?", params, REQUEST_GETDATA);
    }

    public void loadCustomerCare(String key){
        params = new TreeMap<>();
        params.put("url_key", key);
        String languageCode = LanguageUtils.getCurrentLanguageCode();
        String storeId = StoreUtils.getStoreIdByLanguageCode(languageCode
                                                            .equalsIgnoreCase(LanguageUtils.LANGUAGE_CODE_AUTO)
                                                            ? LanguageUtils.LANGUAGE_CODE_ENGLISH
                                                            : languageCode);
        params.put("store_id",storeId);
        requestHttp(HTTP_METHOD.GET, "appservice/cms/customerCare", params, REQUEST_GET_CUSTOMER_CARE);
    }

    @Override
    public void requestHttp(BaseHttp.HTTP_METHOD method, String url, TreeMap map, int requestCode) {
        super.requestHttp(method, url, map, requestCode);
    }

    @Override
    public void onSuccess(int requestCode, String response, Object object) {
        JLogUtils.json("response", response);
        switch (requestCode) {
            case REQUEST_GETDATA:
                if (isOk(response)) {
                    SVRAppserviceCmsCmsPageReturnEntity entity = JJsonUtils.parseJsonObj(response, SVRAppserviceCmsCmsPageReturnEntity.class);
                    postHandler(requestCode, entity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = JJsonUtils.parseJsonObj(response, ErrorMsgBean.class);
                    String errorMsg="";
                    if(bean!=null){
                        errorMsg=bean.getErrorMessage();
                    }
                    postHandler(requestCode,errorMsg, RESPONSE_FAILED);
                }
                break;
            case REQUEST_GET_CUSTOMER_CARE:
                if (isOk(response)) {
                    SVRAppserviceCmsCmsPageReturnEntity entity = JJsonUtils.parseJsonObj(response, SVRAppserviceCmsCmsPageReturnEntity.class);
                    postHandler(requestCode, entity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = JJsonUtils.parseJsonObj(response, ErrorMsgBean.class);
                    String errorMsg="";
                    if(bean!=null){
                        errorMsg=bean.getErrorMessage();
                    }
                    postHandler(requestCode,errorMsg, RESPONSE_FAILED);
                }
                break;
        }
    }

    @Override
    public void onFalied(int requestCode, VolleyError volleyError, Object object, int errorType) {
        postErrorHandler(REQUEST_ERROR, requestCode, volleyError.getMessage(),errorType);
    }
}
