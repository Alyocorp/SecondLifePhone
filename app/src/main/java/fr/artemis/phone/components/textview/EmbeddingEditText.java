package fr.artemis.phone.components.textview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class EmbeddingEditText<T> extends EditText {

	private T attribute;

	private String label;

	public EmbeddingEditText( Context context, AttributeSet set ) {
		super(context, set);
	}

	public EmbeddingEditText( Context context, String label, T attribute ) {
		super( context );
		this.label = label;
		this.attribute = attribute;
	}

	public T getAttribute() {
		return attribute;
	}

	public String getLabel() {
		return label;
	}
}
