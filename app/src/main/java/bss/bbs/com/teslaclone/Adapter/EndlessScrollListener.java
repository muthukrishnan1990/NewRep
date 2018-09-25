package bss.bbs.com.teslaclone.Adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


/**
 * Created by Benchmark on 1/11/2018.
 */

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener{
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 20; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private int current_page = 1;
    private LinearLayoutManager mLinearLayoutManager;

    public EndlessScrollListener(LinearLayoutManager LinearLayoutManager2) {
        this.mLinearLayoutManager = LinearLayoutManager2;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal+1) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            current_page++;
            onLoadMore(current_page);
            loading = true;
        }
    }
    public abstract void onLoadMore(int current_page);
}
