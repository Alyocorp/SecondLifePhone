package fr.artemis.phone.components.advancedrecyclerview;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

class SwipeableViewHolderUtils {

	public static View getSwipeableContainerView( @Nullable RecyclerView.ViewHolder vh ) {
		if( vh instanceof SwipeableItemViewHolder ) {
			return getSwipeableContainerView( ( SwipeableItemViewHolder ) vh );
		} else {
			return null;
		}
	}

	public static View getSwipeableContainerView( @Nullable SwipeableItemViewHolder vh ) {
		if( vh instanceof RecyclerView.ViewHolder ) {
			View containerView = vh.getSwipeableContainerView();
			View itemView = ( ( RecyclerView.ViewHolder ) vh ).itemView;

			if( containerView == itemView ) {
				throw new IllegalStateException( "Inconsistency detected! getSwipeableContainerView() returns itemView" );
			}

			return containerView;
		} else {
			return null;
		}
	}
}