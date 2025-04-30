package fr.artemis.phone.components.advancedrecyclerview.base.viewholders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fr.artemis.phone.components.advancedrecyclerview.ExpandableItemConstants;
import fr.artemis.phone.components.advancedrecyclerview.ExpandableItemState;
import fr.artemis.phone.components.advancedrecyclerview.ExpandableItemStateFlags;


/**
 * <p>Interface which provides required information for expanding item.</p>
 * <p>Implement this interface on your sub-class of the {@link RecyclerView.ViewHolder}.</p>
 */
public interface ExpandableItemViewHolder {

	/**
	 * Sets the state flags value for expanding item
	 *
	 * @param flags Bitwise OR of these flags;
	 *              - {@link ExpandableItemConstants#STATE_FLAG_IS_GROUP}
	 *              - {@link ExpandableItemConstants#STATE_FLAG_IS_CHILD}
	 *              - {@link ExpandableItemConstants#STATE_FLAG_IS_EXPANDED}
	 *              - {@link ExpandableItemConstants#STATE_FLAG_IS_UPDATED}
	 * @see #getExpandState()
	 */
	void setExpandStateFlags( @ExpandableItemStateFlags int flags );

	/**
	 * Gets the state flags value for expanding item. You can access these flags more human friendly way through {@link #getExpandState()}.
	 *
	 * @return Bitwise OR of these flags;
	 * - {@link ExpandableItemConstants#STATE_FLAG_IS_GROUP}
	 * - {@link ExpandableItemConstants#STATE_FLAG_IS_CHILD}
	 * - {@link ExpandableItemConstants#STATE_FLAG_IS_EXPANDED}
	 * - {@link ExpandableItemConstants#STATE_FLAG_IS_UPDATED}
	 * @see #getExpandState()
	 */
	@ExpandableItemStateFlags
	int getExpandStateFlags();

	/**
	 * Gets the state object for expandable item.
	 * This method can be used inside of the {@link RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)}.
	 *
	 * @return {@link ExpandableItemState} object
	 */
	@NonNull
	ExpandableItemState getExpandState();
}