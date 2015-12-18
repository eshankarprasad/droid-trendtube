package org.trendtube.app.interfaces;

import org.trendtube.app.model.BasicItem;

/**
 * Created by shankar on 15/12/15.
 */
public interface FragmentListener {

    public void callRegionUpdate(BasicItem selectedRegion);
    public void callCategoryUpdate(BasicItem selectedCategory);
}
