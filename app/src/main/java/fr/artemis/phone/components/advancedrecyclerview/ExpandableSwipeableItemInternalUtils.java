package fr.artemis.phone.components.advancedrecyclerview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class ExpandableSwipeableItemInternalUtils {

	private ExpandableSwipeableItemInternalUtils() {
	}

	@SuppressWarnings( "unchecked" )
	public static SwipeResultAction invokeOnSwipeItem(
	@NonNull BaseExpandableSwipeableItemAdapter<?, ?> adapter,
	@NonNull RecyclerView.ViewHolder holder,
	int groupPosition, int childPosition, int result ) {

		if( childPosition == RecyclerView.NO_POSITION ) {
			return ( ( ExpandableSwipeableItemAdapter ) adapter ).onSwipeGroupItem( holder, groupPosition, result );
		} else {
			return ( ( ExpandableSwipeableItemAdapter ) adapter ).onSwipeChildItem( holder, groupPosition, childPosition, result );
		}
	}
}