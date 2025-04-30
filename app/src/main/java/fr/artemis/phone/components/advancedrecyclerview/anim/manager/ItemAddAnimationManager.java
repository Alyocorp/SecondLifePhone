package fr.artemis.phone.components.advancedrecyclerview.anim.manager;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import fr.artemis.phone.components.advancedrecyclerview.anim.animation.AddAnimationInfo;
import fr.artemis.phone.components.advancedrecyclerview.anim.animator.BaseItemAnimator;

public abstract class ItemAddAnimationManager extends BaseItemAnimationManager<AddAnimationInfo> {

	private static final String TAG = "ARVItemAddAnimMgr";

	public ItemAddAnimationManager( BaseItemAnimator itemAnimator ) {
		super( itemAnimator );
	}

	@Override
	public long getDuration() {
		return mItemAnimator.getAddDuration();
	}

	@Override
	public void setDuration( long duration ) {
		mItemAnimator.setAddDuration( duration );
	}

	@Override
	public void dispatchStarting( @NonNull AddAnimationInfo info, @NonNull RecyclerView.ViewHolder item ) {
		if ( debugLogEnabled() ) {
			Log.d( TAG, "dispatchAddStarting(" + item + ")" );
		}
		mItemAnimator.dispatchAddStarting( item );
	}

	@Override
	public void dispatchFinished( @NonNull AddAnimationInfo info, @NonNull RecyclerView.ViewHolder item ) {
		if ( debugLogEnabled() ) {
			Log.d( TAG, "dispatchAddFinished(" + item + ")" );
		}
		mItemAnimator.dispatchAddFinished( item );
	}

	@Override
	protected boolean endNotStartedAnimation( @NonNull AddAnimationInfo info, @Nullable RecyclerView.ViewHolder item ) {
		if ( ( info.getAvailableViewHolder() != null ) && ( ( item == null ) || ( info.getAvailableViewHolder() == item ) ) ) {
			onAnimationEndedBeforeStarted( info, info.getAvailableViewHolder() );
			dispatchFinished( info, info.getAvailableViewHolder() );
			info.clear( info.getAvailableViewHolder() );
			return true;
		} else {
			return false;
		}
	}

	public abstract boolean addPendingAnimation( RecyclerView.ViewHolder item );
}