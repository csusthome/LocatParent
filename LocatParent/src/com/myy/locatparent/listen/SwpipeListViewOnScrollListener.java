package com.myy.locatparent.listen;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

/** ����Listview������ˢ�µ�Scroll�¼���ͻ, ʹ�����ScrollListener���Ա���Listview�����쳣 */
public class SwpipeListViewOnScrollListener implements AbsListView.OnScrollListener {

    private SwipeRefreshLayout mSwipeView;
    private AbsListView.OnScrollListener mOnScrollListener;

    public SwpipeListViewOnScrollListener(SwipeRefreshLayout swipeView) {
        mSwipeView = swipeView;
    }

    public SwpipeListViewOnScrollListener(SwipeRefreshLayout swipeView,
            OnScrollListener onScrollListener) {
        mSwipeView = swipeView;
        mOnScrollListener = onScrollListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        View firstView = absListView.getChildAt(firstVisibleItem);

        // ��firstVisibleItem�ǵ�0λ�����firstView==null˵���б�Ϊ�գ���Ҫˢ��;����top==0˵���Ѿ������б���, Ҳ��Ҫˢ��
        if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == 0)) {
            mSwipeView.setEnabled(true);
        } else {
            mSwipeView.setEnabled(false);
        }
        if (null != mOnScrollListener) {
            mOnScrollListener.onScroll(absListView, firstVisibleItem,
                    visibleItemCount, totalItemCount);
        }
    }
}
