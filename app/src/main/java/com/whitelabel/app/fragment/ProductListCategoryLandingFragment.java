package com.whitelabel.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.activity.ProductListActivity;
import com.whitelabel.app.activity.ShoppingCartActivity1;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.listener.OnFilterSortFragmentListener;
import com.whitelabel.app.model.CategoryBaseBean;
import com.whitelabel.app.model.SVRAppserviceProductSearchFacetsReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchParameter;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductEntity;
import com.whitelabel.app.model.TMPProductListFilterSortPageEntity;
import com.whitelabel.app.model.TMPProductListListPageEntity;
import com.whitelabel.app.model.TempCategoryBean;
import com.whitelabel.app.utils.FilterSortHelper;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JStorageUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.CustomTabCustomPageIndicator;
import com.whitelabel.app.widget.FilterSortBottomView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imaginato on 2015/7/14.
 */
public class ProductListCategoryLandingFragment extends ProductListBaseFragment implements View.OnClickListener,
    ViewPager.OnPageChangeListener, OnFilterSortFragmentListener,FilterSortBottomView.FilterSortBottomViewCallBack {
    private final String TAG = "ProductListCategoryLandingFragment";
    protected ProductListActivity productListActivity;
    protected View mContentView;
    private String categoryId;
    private CustomTabCustomPageIndicator ctpiCategoryList;
    private ArrayList<CategoryBaseBean.CategoryBean.ChildrenBeanX.ChildrenBean> categoryArrayList;
    private ProductListFilterFragment filterFragment;
    private ProductListSortFragment sortFragment;
    private FilterSortHelper filterSortHelper;
    public FilterSortBottomView filterSortBottomView;
    private ArrayList<ProductListProductListFragment> categoryProductListFragmentArrayList;
    public ImageView mTopViewToggleIV;
    public RelativeLayout mTopFilterAndSortBarRL;
    public boolean mIsShowSwitchFilterBar;
    public ImageView mIVBottomSlideToTop;
    private TempCategoryBean tempCategoryBean;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            productListActivity = (ProductListActivity) context;
            filterFragment = new ProductListFilterFragment();
            filterFragment.setFragmentListener(this);
            sortFragment = new ProductListSortFragment();
            sortFragment.setFragmentListener(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_productlist_categorylanding, null);
        tempCategoryBean = TempCategoryBean.getInstance();
        tempCategoryBean.mGATrackTimeStart= GaTrackHelper.getInstance().googleAnalyticsTimeStart();
        setContentView(mContentView);
        return mContentView;
    }

    private void skipSearchPage(){
        productListActivity.switchFragment(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY, ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, null);
    }

    private void gotoShoppingCartActivity(){
        if (productListActivity != null) {
            Intent intent = new Intent(productListActivity, ShoppingCartActivity1.class);
            startActivity(intent);
        }
    }

    private void jumpLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginRegisterActivity.class);
        startActivityForResult(intent, 1000);
        getActivity().overridePendingTransition(R.anim.enter_bottom_top, R.anim.exit_bottom_top);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_shopping_cart:
                if(WhiteLabelApplication.getAppConfiguration().isSignIn(getActivity())) {
                    gotoShoppingCartActivity();
                }else{
                    jumpLoginActivity();
                }
                break;
            case R.id.action_search:

                if (productListActivity != null) {
                    filterSortBottomView.hideSwitchAndFilterBar(true);
                    skipSearchPage();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void initToolBar(){
        setLeftMenuIcon(JViewUtils.getNavBarIconDrawable(getActivity(),R.drawable.ic_action_back));
        setLeftMenuClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

                // clear all product search parameter
                tempCategoryBean.clearSVRAppserviceProductSearchParameterByCategory();
            }
        });

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initToolBar();
        int FRAGMENT_CONTAINER_ID = R.id.fl_sort_container;/*R.id.flFilterSortContainer;*/
//        try {
//            GaTrackHelper.getInstance().googleAnalytics("Sub Category Screen", getActivity());
//            JLogUtils.i("googleAnalytics", "Sub Category Screen");
//
//        }catch (Exception ex){
//            ex.getStackTrace();
//        }

        mIVBottomSlideToTop = (ImageView) mContentView.findViewById(R.id.iv_bottom_slideto_top);
        mTopFilterAndSortBarRL = (RelativeLayout) mContentView.findViewById(R.id.top_switch_and_filter_bar);
        mTopViewToggleIV = (ImageView) mContentView.findViewById(R.id.iv_view_toggle_top);
        LinearLayout mTopFilterLL = (LinearLayout) mContentView.findViewById(R.id.ll_filter_top);
        LinearLayout mTopSortLL = (LinearLayout) mContentView.findViewById(R.id.ll_sort_top);
        mTopFilterLL.setOnClickListener(this);
        mTopSortLL.setOnClickListener(this);
        mTopViewToggleIV.setOnClickListener(this);
        mIVBottomSlideToTop.setOnClickListener(this);
        filterSortBottomView = new FilterSortBottomView();
        filterSortBottomView.initView(mTopFilterAndSortBarRL, mIVBottomSlideToTop, this);
        ctpiCategoryList = (CustomTabCustomPageIndicator) mContentView.findViewById(R.id.ctpiCategoryList);
        ctpiCategoryList.setIndicatorColorResource(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        ctpiCategoryList.setOnPageChangeListener(this);
        ViewPager vpProductList = (ViewPager) mContentView.findViewById(R.id.vpProductList);
        FrameLayout flFilterSortContainer = (FrameLayout) mContentView.findViewById(R.id.flFilterSortContainer);
        flFilterSortContainer.setOnClickListener(this);
        if(getArguments()!=null) {
            TMPProductListListPageEntity tmpProductListListPageEntity = (TMPProductListListPageEntity) getArguments().getSerializable("data");
            categoryId=tmpProductListListPageEntity.getCategoryId();
        }
        String allCategoryName = null;
        int parentCategoryIndex = 0;
        int categoryViewCount = 0;
        categoryArrayList = new ArrayList<>();
        CategoryBaseBean.CategoryBean.ChildrenBeanX entity = tempCategoryBean.getSearchCategoryEntity();

        //all暂时取消掉  ray
        SVRAppserviceProductSearchParameter  parameter= tempCategoryBean.getSVRAppserviceProductSearchParameterById(productListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS,-1);
        //需求隐藏 Gem Brands 的ALL  GEM Brands 的
        String gemBradns=productListActivity.getResources().getString(R.string.gembrand);
        boolean   isGemBrands=false;
        if(!TextUtils.isEmpty(parameter.getName())&&gemBradns.equals(parameter.getName().replace(" ","").toUpperCase())){
            isGemBrands=true;
        }
        if ((entity != null) && (!JDataUtils.isEmpty(entity.getId()))) {
            CategoryBaseBean.CategoryBean.ChildrenBeanX.ChildrenBean allCategory = new CategoryBaseBean.CategoryBean.ChildrenBeanX.ChildrenBean();
            allCategory.setId(entity.getId());
            allCategory.setName(getString(R.string.productlist_categorylanding_allcategory));
            allCategory.setLevel(entity.getLevel());
//            allCategory.setChildren(null);
            allCategoryName = entity.getMenuTitle();
            if (!JDataUtils.isEmpty(allCategoryName)) {
                allCategoryName = allCategoryName.toUpperCase();
            }

            if (entity.getChildren() != null && entity.getChildren().size() > 0) {
                final int categoryChildrenSize = entity.getChildren().size();
                categoryViewCount = categoryChildrenSize;
                parentCategoryIndex = 0;
                for (int index = 0; index < categoryChildrenSize; ++index) {
                    if(!isGemBrands) {//当为GEM Brands  隐藏ALL
                        //是第0个并且entity的第0个不是all,防止重复添加
                        if (index == parentCategoryIndex&&entity.getId()!=entity.getChildren().get(parentCategoryIndex).getId()) {
                            //TODO temp not add all
                            categoryArrayList.add(allCategory);
                        }
                    }
                    categoryArrayList.add(entity.getChildren().get(index));
                }
            } else {
                if(!isGemBrands) {
                    categoryArrayList.add(allCategory);
                }
            }
        }
        setTitle(allCategoryName);
        if(categoryArrayList!=null&& categoryId !=null&&!"0".equals(categoryId)) {
            for (int i = 0; i < categoryArrayList.size(); i++) {
                if(categoryArrayList.get(i).getId().equals(categoryId)){
                    parentCategoryIndex=i;
                }

                tempCategoryBean.initSVRAppserviceProductFilterSelectedItemList(i);
            }
        }
        if(getActivity()!=null&&!getActivity().isFinishing()&&isAdded()) {
            //默认选择的 page由 currentProductListFragmentPosition优先控制
            if(tempCategoryBean.getCurrentProductListFragmentPosition()==0) {
                tempCategoryBean.setCurrentProductListFragmentPosition(parentCategoryIndex);
            }
            JLogUtils.i("Martin", "currentProductListFragmentPosition=>" + parentCategoryIndex);
            CustomTabPageIndicatorAdapter fragmentPagerAdapter = new CustomTabPageIndicatorAdapter(getChildFragmentManager());
            vpProductList.setAdapter(fragmentPagerAdapter);

            ctpiCategoryList.setViewPager(vpProductList);

            vpProductList.setOffscreenPageLimit(categoryViewCount);
            vpProductList.addOnPageChangeListener(this);
            vpProductList.setCurrentItem(tempCategoryBean.getCurrentProductListFragmentPosition());
            JLogUtils.i("ray","currlog:"+tempCategoryBean.getCurrentProductListFragmentPosition());
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    ctpiCategoryList.setSelectColor(tempCategoryBean.getCurrentProductListFragmentPosition());

                }
            });
        }

        filterSortHelper = new FilterSortHelper(getActivity(), sortFragment, filterFragment, flFilterSortContainer, FRAGMENT_CONTAINER_ID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        categoryProductListFragmentArrayList.get(tempCategoryBean.getCurrentProductListFragmentPosition()).onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public long getCartItemCount(){
        long cartItemCount = 0;
        try{
            if (WhiteLabelApplication.getAppConfiguration().isSignIn(getActivity())) {
                cartItemCount = WhiteLabelApplication.getAppConfiguration().getUserInfo(getActivity()).getCartItemCount();
                ArrayList<TMPLocalCartRepositoryProductEntity> list = JStorageUtils.getProductListFromLocalCartRepository(getActivity());
                if (list.size() > 0) {
                    for (TMPLocalCartRepositoryProductEntity localCartRepositoryProductEntity : list) {
                        cartItemCount += localCartRepositoryProductEntity.getSelectedQty();
                    }
                }
            } else {
                ArrayList<TMPLocalCartRepositoryProductEntity> list = JStorageUtils.getProductListFromLocalCartRepository(getActivity());
                if (list.size() > 0) {
                    for (TMPLocalCartRepositoryProductEntity localCartRepositoryProductEntity : list) {
                        cartItemCount += localCartRepositoryProductEntity.getSelectedQty();
                    }
                }
            }
        }catch (Exception ex){
            ex.getStackTrace();
        }
        return cartItemCount;
    }
    @Override
    public void onResume() {
        super.onResume();

        long cartItemcount=getCartItemCount();
        updateRightIconNum(R.id.action_shopping_cart, cartItemcount);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_bottom_slideto_top:
                onSlideToTop();
                break;
            case R.id.ll_sort_top:
                productListActivity.filterSortOption(productListActivity.TYPE_SORT);
                break;
            case R.id.ll_filter_top:
                productListActivity.filterSortOption(productListActivity.TYPE_FILTER);
                break;
            case R.id.iv_view_toggle_top:
                int position=tempCategoryBean.getCurrentProductListFragmentPosition();
                if(position<categoryProductListFragmentArrayList.size()) {
                    categoryProductListFragmentArrayList.get(position).toggleViewOption();
                }
                break;
        }
    }

    /**
     * @param index From 0 to X
     */
    private ProductListProductListFragment getCategoryProductListFragmentById(int index) {
        if (categoryProductListFragmentArrayList == null || index < 0) {
            return null;
        }
        if (categoryProductListFragmentArrayList.size() > index) {
            return categoryProductListFragmentArrayList.get(index);
        } else {
            return null;
        }
    }

    /**
     * @param index From 0 to X
     */
    private void setCategoryProductListFragment(int index, ProductListProductListFragment fragment) {
        if (index < 0) {
            return;
        }

        if (categoryProductListFragmentArrayList == null) {
            categoryProductListFragmentArrayList = new ArrayList<>();
        }

        int arraySize = categoryProductListFragmentArrayList.size();
        if (index >= arraySize) {
            for (int newInstallIndex = arraySize; newInstallIndex <= index; ++newInstallIndex) {
                if (newInstallIndex == index) {
                    categoryProductListFragmentArrayList.add(fragment);
                    break;
                } else {
                    categoryProductListFragmentArrayList.add(new ProductListProductListFragment());
                    continue;
                }
            }
        } else {
            categoryProductListFragmentArrayList.set(index, fragment);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        ProductListProductListFragment fragment = getCategoryProductListFragmentById(position);
        if (fragment != null && fragment.getProductItemEntityArrayList() != null && fragment.getProductItemEntityArrayList().size() > 0) {
            filterSortBottomView.hideSwitchAndFilterBar(false);
        } else {
            filterSortBottomView.hideSwitchAndFilterBar(true);
        }
        tempCategoryBean.setCurrentProductListFragmentPosition(position);
        gaScreenName(position);
    }

    private void gaScreenName(int position){
        try {
            String rightTopTitle=tempCategoryBean.searchCategoryEntity.getMenuTitle();
            List<CategoryBaseBean.CategoryBean.ChildrenBeanX.ChildrenBean> parent = tempCategoryBean.searchCategoryEntity.getChildren();
            String rightSubTitle;
            //temp add pos 0 item all
            if (position==0){
                rightSubTitle="All";
            }else {
                position=position-1;
                CategoryBaseBean.CategoryBean.ChildrenBeanX.ChildrenBean returnEntity = parent.get(position);
                rightSubTitle=returnEntity.getMenuTitle();
            }
            StringBuilder builder=new StringBuilder();
            builder.append("Category_");
            builder.append(tempCategoryBean.leftMenuTitle);
            builder.append("_");
            builder.append(rightTopTitle);
            builder.append("_");
            builder.append(rightSubTitle);
            GaTrackHelper.getInstance().googleAnalytics(builder.toString(),getActivity());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

        resetSelection();
    }

    @Override
    public void onSlideToTop() {
        int position=tempCategoryBean.getCurrentProductListFragmentPosition();
        if(position<categoryProductListFragmentArrayList.size()) {
            categoryProductListFragmentArrayList.get(position).onSlideToTop();
        }
    }

    @Override
    public void onSearchFilter() {
        int position = ctpiCategoryList.getCurrentPosition();
        ProductListProductListFragment productListProductListFragment = categoryProductListFragmentArrayList.get(position);
        productListProductListFragment.onSearchFilter();
    }

    @Override
    public SVRAppserviceProductSearchFacetsReturnEntity getFilterInfo() {
        int position = ctpiCategoryList.getCurrentPosition();
        ProductListProductListFragment productListProductListFragment = categoryProductListFragmentArrayList.get(position);
        return productListProductListFragment.getFilterInfo();
    }

    public int getCurrentPagePosition(){
        return ctpiCategoryList.getCurrentPosition();
    }

    @Override
    public void onBackPressed() {
        if (productListActivity != null) {
            if (filterSortHelper.isAnyActive()) {

                // hide sort fragment
                resetSelection();
                //filterSortDefault();
            } else {
                productListActivity.finish();
            }

            // clear search parameter for category
            tempCategoryBean.clearSVRAppserviceProductSearchParameterByCategory();

            // clear selected filter item for category
            tempCategoryBean.clearSVRAppserviceProductFilterSelectedItemByCategory();
        }
    }

    @Override
    public void onFilterWidgetClick(boolean show) {

        // hide sort and reset sort button state
        resetSelection();
    }

    @Override
    public void onSortWidgetClick(boolean show) {
        if (productListActivity != null) {
            filterSortHelper.onSortClicked(show, createBundle());
        }
    }

    @Override
    public void onViewToggleChanged() {
        // hide sort and reset sort button state
        resetSelection();
    }

    private Bundle createBundle() {
        TMPProductListFilterSortPageEntity filterSortPageEntity = new TMPProductListFilterSortPageEntity();
        filterSortPageEntity.setPreviousFragmentType(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY);
        filterSortPageEntity.setCategoryFragmentPosition(tempCategoryBean.getCurrentProductListFragmentPosition());

        ProductListProductListFragment productListFragment = getCategoryProductListFragmentById(tempCategoryBean.getCurrentProductListFragmentPosition());

        if (productListFragment != null) {
            filterSortPageEntity.setFacets(productListFragment.getSearchReturnEntityFacets());
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", filterSortPageEntity);
        return bundle;
    }

    @Override
    public void onCancelClick(View view) {
        resetSelection();
    }

    @Override
    public void onFilterSortListItemClick(int type, Object object) {
        filterSortDefault();
    }

    @Override
    public void onAnimationFinished(Fragment fragment) {
        filterSortHelper.hideContainer(fragment);
    }

    private void filterSortDefault() {
        ProductListProductListFragment currentProductListFragment = getCategoryProductListFragmentById(tempCategoryBean.getCurrentProductListFragmentPosition());
        resetSelection();
        if (currentProductListFragment != null) {
            currentProductListFragment.searchByType(ProductListProductListFragment.SEARCH_TYPE_INIT);
        }
    }

    private void resetSelection() {
        if(productListActivity != null){
            // reset sort button state
            productListActivity.resetCurrentFilterSortTabIndex();
        }
        filterSortHelper.hideVisibleFragments();
    }

    @Override
    public int getCurrentFilterSortTabIndex() {
        return tempCategoryBean.getCurrentFilterSortTabIndex();
    }

    class CustomTabPageIndicatorAdapter extends FragmentPagerAdapter {
        public CustomTabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            ProductListProductListFragment productListProductListFragment = new ProductListProductListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("fragmentType", ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY);
            bundle.putInt("position", position);
            productListProductListFragment.setArguments(bundle);

            setCategoryProductListFragment(position, productListProductListFragment);

            String categoryId = null;
            String brandId = null;
            String brandName=null;
            if (categoryArrayList != null && position >= 0 && categoryArrayList.size() > position) {
                CategoryBaseBean.CategoryBean.ChildrenBeanX.ChildrenBean category = categoryArrayList.get(position);
                if (category != null) {
                    categoryId = category.getId();
                    brandId = category.getBrandId();
                    brandName=category.getBrandName();

                }
            }
            tempCategoryBean.setSVRAppserviceProductSearchParameterCategoryId(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY, position, categoryId);
            tempCategoryBean.setSVRAppserviceProductSearchParameterBrandId(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY, position, brandId);
            //tempCategoryBean.setSVRAppserviceProductSearchParameterBrandName(position, brandName);

            return productListProductListFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String categoryName = null;
            if (categoryArrayList != null && position >= 0 && categoryArrayList.size() > position) {
                final int categoryArrayListSize = categoryArrayList.size();
                CategoryBaseBean.CategoryBean.ChildrenBeanX.ChildrenBean category = categoryArrayList.get(position % categoryArrayListSize);
                if (category != null) {
                    categoryName = category.getName();
                }
            }
            return categoryName;
        }

        @Override
        public int getCount() {
            int count = 0;
            if (categoryArrayList != null) {
                count = categoryArrayList.size();
            }
            return count;
        }
    }
}
