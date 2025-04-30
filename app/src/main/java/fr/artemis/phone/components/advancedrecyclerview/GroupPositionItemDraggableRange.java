package fr.artemis.phone.components.advancedrecyclerview;

import androidx.annotation.NonNull;

public class GroupPositionItemDraggableRange extends ItemDraggableRange {

	public GroupPositionItemDraggableRange( int start, int end ) {
		super( start, end );
	}

	@NonNull
	protected String getClassName() {
		return "GroupPositionItemDraggableRange";
	}
}