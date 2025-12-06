package com.comp2042.data;

import com.comp2042.view.ViewData;

/**
 * This class represents the result of a downward movement of a brick.
 *
 * <p>
 * This class acts as a data container that stores results after a
 * downward movement of a brick. It includes information about the
 * cleared rows using {@link ClearRow} and the new game matrix using {@link ViewData}.
 * </p>
 */
public final class DownData {
    /**
     * Stores information on the cleared rows
     */
    private final ClearRow clearRow;
    /**
     * Stores the current game display
     */
    private final ViewData viewData;

    /**
     * Creates a new {@link DownData} object containing information on
     * the cleared rows using {@link ClearRow} and the new game display
     * using {@link ViewData}.
     *
     * @param clearRow object that stores information on the cleared rows
     * @param viewData object that stores the new game display
     */
    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }


    /**
     * Returns the information of the cleared rows using {@link ClearRow}
     *
     * @return {@link ClearRow} object
     */

    public ClearRow getClearRow() {
        return clearRow;
    }


    /**
     * Returns the information on the new game display using {@link ViewData}.
     *
     * @return {@link ViewData} object
     */
    public ViewData getViewData() {
        return viewData;
    }
}
