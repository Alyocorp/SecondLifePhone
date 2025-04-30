package fr.artemis.phone.components.advancedrecyclerview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapter path segment
 */
public class AdapterPathSegment {

	/**
	 * Adapter
	 */
	@NonNull
	public final RecyclerView.Adapter adapter;

	/**
	 * Tag object
	 */
	@Nullable
	public final Object tag;

	/**
	 * Constructor.
	 *
	 * @param adapter The adapter
	 * @param tag     The tag object
	 */
	AdapterPathSegment( @NonNull RecyclerView.Adapter adapter, @Nullable Object tag ) {
		this.adapter = adapter;
		this.tag = tag;
	}
}