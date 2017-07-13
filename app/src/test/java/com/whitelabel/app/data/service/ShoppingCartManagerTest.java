package com.whitelabel.app.data.service;

import com.whitelabel.app.SessionKeyProvider;
import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.data.preference.ICacheApi;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.ShoppingCartListEntityCart;
import com.whitelabel.app.model.ShoppingCartVoucherApplyEntity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.observers.TestSubscriber;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/7/12.
 */
public class ShoppingCartManagerTest {

    ShoppingCartManager  shoppingCartManager;
    String sessionKey;
    @Mock
    ICacheApi iCacheApi;
    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        sessionKey=new SessionKeyProvider().getSession();
        shoppingCartManager=new ShoppingCartManager(DataManager.getInstance().getShoppingCartApi(),iCacheApi);
    }
    //coupon_code=test&platformId=2&remove=0&serviceVersion=1.0.5&session_key=7daca5d0571aa507ebcd9e2b3a2c6362&versionNumber=1.0.2&
//    session_key=7eae4d1154d0f5587ad2802193ae08be&coupon_code=test&remove=0
    @Test
    public void getShoppingCartInfo() throws Exception {
        TestSubscriber<ShoppingCartListEntityCart> testSubscriber=TestSubscriber.create();
        shoppingCartManager.getShoppingCartInfo(sessionKey)
                .subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        ShoppingCartListEntityCart shoppingCartListEntityCart=testSubscriber.getOnNextEvents().get(0);
        Assert.assertTrue(shoppingCartListEntityCart.getItems()!=null);
    }
    @Test
    public void applyOrCancelVercherCode() throws Exception {
        TestSubscriber<ShoppingCartVoucherApplyEntity>  testSubscriber=TestSubscriber.create();
        shoppingCartManager.applyOrCancelVercherCode(sessionKey,"123","0")
        .subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        ShoppingCartVoucherApplyEntity shoppingCartVoucherApplyEntity= testSubscriber.getOnNextEvents().get(0);
        Assert.assertTrue(shoppingCartVoucherApplyEntity.getStatus()==-1);
        TestSubscriber<ShoppingCartVoucherApplyEntity>  testSubscriber1=TestSubscriber.create();
         shoppingCartManager.applyOrCancelVercherCode(sessionKey,"test","0").
                 subscribe(testSubscriber1);
         testSubscriber.assertNoErrors();
         testSubscriber.assertCompleted();
        ShoppingCartVoucherApplyEntity shoppingCartVoucherApplyEntity1=  testSubscriber1.getOnNextEvents().get(0);
        Assert.assertTrue(shoppingCartVoucherApplyEntity1.getStatus()==1);
    }
    @Test
    public void checkoutOfStock() throws Exception {
        TestSubscriber<ResponseModel> testSubscriber=new TestSubscriber<>();
        shoppingCartManager.checkoutOfStock(sessionKey).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        ResponseModel responseModel=testSubscriber.getOnNextEvents().get(0);
        Assert.assertTrue(responseModel.getStatus()==1);

    }


}
