package filter;

import javax.servlet.Filter;

/**
 * 过滤器委托.(过滤器类)
 * Created by com on 2017/8/21.
 */
public class FilterDelegator {
    private Filter filter;

    private String filterName;

    public FilterDelegator(Filter filter) {
        this.filter = filter;
    }

    public FilterDelegator(Filter filter, String filterName) {

        this.filter = filter;
        this.filterName = filterName;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }
}
