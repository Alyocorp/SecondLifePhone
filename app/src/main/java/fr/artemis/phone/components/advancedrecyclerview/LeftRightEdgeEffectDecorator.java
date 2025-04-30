package fr.artemis.phone.components.advancedrecyclerview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class LeftRightEdgeEffectDecorator extends BaseEdgeEffectDecorator {

	public LeftRightEdgeEffectDecorator( @NonNull RecyclerView recyclerView ) {
		super( recyclerView );
	}

	@Override
	protected int getEdgeDirection( int no ) {
		switch( no ) {
			case 0:
				return EDGE_LEFT;
			case 1:
				return EDGE_RIGHT;
			default:
				throw new IllegalArgumentException();
		}
	}
}