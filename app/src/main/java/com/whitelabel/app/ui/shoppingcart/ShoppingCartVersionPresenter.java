package com.whitelabel.app.ui.shoppingcart;

import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.data.service.IShoppingCartManager;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.ShoppingCartListEntityCart;
import com.whitelabel.app.model.ShoppingDiscountBean;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.utils.ExceptionParse;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.RxUtil;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Administrator on 2017/7/12.
 */
public class ShoppingCartVersionPresenter extends RxPresenter<ShoppingCartVersionContract.View > implements ShoppingCartVersionContract.Presenter {
    private IBaseManager iBaseManager;
    public ShoppingCartVersionPresenter(IBaseManager iBaseManager){
        this.iBaseManager=iBaseManager;
    }

    @Override
    public void versionCheck() {
        Subscription subscription= iBaseManager.versionCheck().compose(RxUtil.<ResponseModel>rxSchedulerHelper()).subscribe(
            new Subscriber<ResponseModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {
                    if(ExceptionParse.parseException(throwable).getErrorType()== ExceptionParse.ERROR.HTTP_ERROR) {
                        mView.showErrorMessage(ExceptionParse.parseException(throwable).getErrorMsg());
                    }
                }

                @Override
                public void onNext(ResponseModel responseModel) {
                    if (responseModel.getStatus()==1){
                        mView.checkCartStock();
                    }else if (responseModel.getStatus()==-1){
                        //need update
                        mView.showUpdateDialog();
                    }
                }
            });
        addSubscrebe(subscription);
    }
}
