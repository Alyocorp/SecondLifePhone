package fr.artemis.phone.components.advancedrecyclerview;

import androidx.annotation.NonNull;

public class ChildPositionItemDraggableRange extends ItemDraggableRange {

	public ChildPositionItemDraggableRange( int start, int end ) {
		super( start, end );
	}

	@NonNull
	protected String getClassName() {
		return "ChildPositionItemDraggableRange";
	}
}