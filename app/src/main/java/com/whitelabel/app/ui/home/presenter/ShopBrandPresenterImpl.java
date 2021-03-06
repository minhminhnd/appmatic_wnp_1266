package com.whitelabel.app.ui.home.presenter;

import com.whitelabel.app.Const;
import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.data.service.ICommodityManager;
import com.whitelabel.app.model.ShopBrandResponse;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.ui.home.ShopBrandContract;
import com.whitelabel.app.utils.ExceptionParse;
import com.whitelabel.app.utils.RxUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by ray on 2017/4/13.
 */

public class ShopBrandPresenterImpl extends RxPresenter<ShopBrandContract.View> implements ShopBrandContract.Presenter{

    private ICommodityManager  iCommodityManager;
    private IBaseManager  iBaseManager;

    @Inject
    public ShopBrandPresenterImpl(ICommodityManager iCommodityManager, IBaseManager iBaseManager){
        this.iCommodityManager=iCommodityManager;
        this.iBaseManager=iBaseManager;
    }

    @Override
    public void getOnlineCategoryDetail(boolean isCache,final String categoryId) {
//        mView.showProgressDialog();
        mView.showSwipeLayout();
        final String sessionKey=iBaseManager.isSign()?iBaseManager.getUser().getSessionKey():"";
        Subscription subscription=iCommodityManager.getShopBrandDetail(isCache,categoryId,sessionKey)
                .compose(RxUtil.<ShopBrandResponse>rxSchedulerHelper())
                .subscribe(new Subscriber<ShopBrandResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
//                        mView.closeProgressDialog();
                        mView.closeSwipeLayout();
                        mView.showErrorMsg(ExceptionParse.parseException(throwable).getErrorMsg());
                        if(ExceptionParse.parseException(throwable).getErrorType()== ExceptionParse.ERROR.HTTP_ERROR) {
                            mView.showOnlineErrorLayout();
                        }
                    }

                    @Override
                    public void onNext(ShopBrandResponse shopBrandResponse) {
//                        mView.closeProgressDialog();
                        mView.closeSwipeLayout();
                        mView.hideOnlineErrorLayout();
                        ArrayList<ShopBrandResponse.BrandsBean.ItemsBean> titleAndItemList = createTitleAndItemList(shopBrandResponse.getBrands());
                        mView.loadData(titleAndItemList);
                        mView.loadTitleData(getTitleList(titleAndItemList));
                    }
                });
        addSubscrebe(subscription);
    }

    /**
     * base net Response ,to create recyclerview use list
     * @param brandsBeans net response data
     * @return
     */
    private ArrayList<ShopBrandResponse.BrandsBean.ItemsBean> createTitleAndItemList(List<ShopBrandResponse.BrandsBean> brandsBeans) {
        int currentTitlePosition = 0;
        int currentItemPosition;
        ArrayList lists=new ArrayList<>();
        if (brandsBeans!=null&&!brandsBeans.isEmpty()){
            for (int i=0;i<brandsBeans.size();i++){
                ShopBrandResponse.BrandsBean brandsBean = brandsBeans.get(i);
                ShopBrandResponse.BrandsBean.ItemsBean itemsBean=new ShopBrandResponse.BrandsBean.ItemsBean();
                itemsBean.setTitle(brandsBean.getTitle());
                if (i==0){
                    currentTitlePosition=i;
                }else {
                    //this currentTitlePosition after first,second and third titlePosition ... = currentTitlePositon + last list's size + 1 ,to create next new TitlePosition
                    currentTitlePosition=currentTitlePosition+brandsBeans.get(i-1).getItems().size() +1 ;
                }
                itemsBean.setItemType(Const.HEADER);
                itemsBean.setPosition(currentTitlePosition);
                lists.add(itemsBean);
                if (brandsBean.getItems()!=null && !brandsBean.getItems().isEmpty()){
                    List<ShopBrandResponse.BrandsBean.ItemsBean> items = brandsBean.getItems();
                    for (int j=0;j<items.size();j++){
                        ShopBrandResponse.BrandsBean.ItemsBean itemsBeanChild = items.get(j);
                        ShopBrandResponse.BrandsBean.ItemsBean itemsChildBean=new ShopBrandResponse.BrandsBean.ItemsBean();
                        itemsChildBean.setItemType(Const.ITEM);
                        itemsChildBean.setTitle(itemsBeanChild.getTitle());
                        itemsChildBean.setIcon(itemsBeanChild.getIcon());
                        itemsChildBean.setLink(itemsBeanChild.getLink());
                        itemsChildBean.setId(itemsBeanChild.getId());
                        itemsChildBean.setIdentifier(itemsBeanChild.getIdentifier());
                        currentItemPosition=currentTitlePosition+j+1;
                        itemsChildBean.setPosition(currentItemPosition);
                        lists.add(itemsChildBean);
                    }
                }
            }
        }
        return lists;
    }

    /**
     * base net Response ,to create title list
     * @param brandsBeans net response data
     * @return
     */
    private ArrayList<ShopBrandResponse.BrandsBean.ItemsBean> getTitleList(ArrayList<ShopBrandResponse.BrandsBean.ItemsBean> brandsBeans){
        ArrayList lists=new ArrayList<>();
        if (brandsBeans!=null && !brandsBeans.isEmpty()){
            for (ShopBrandResponse.BrandsBean.ItemsBean itemsBean:brandsBeans){
                if (Const.HEADER==itemsBean.getItemType()){
                    ShopBrandResponse.BrandsBean.ItemsBean bean=new ShopBrandResponse.BrandsBean.ItemsBean();
                    bean.setTitle(itemsBean.getTitle());
                    bean.setPosition(itemsBean.getPosition());
                    lists.add(bean);
                }
            }
        }
        return lists;
    }
}
