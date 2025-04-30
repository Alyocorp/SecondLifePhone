package fr.artemis.phone.components.advancedrecyclerview.anim.manager;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import fr.artemis.phone.components.advancedrecyclerview.anim.animation.ChangeAnimationInfo;
import fr.artemis.phone.components.advancedrecyclerview.anim.animator.BaseItemAnimator;

public abstract class ItemChangeAnimationManager extends BaseItemAnimationManager<ChangeAnimationInfo> {

	private static final String TAG = "ARVItemChangeAnimMgr";

	public ItemChangeAnimationManager( @NonNull BaseItemAnimator itemAnimator ) {
		super( itemAnimator );
	}

	@Override
	public void dispatchStarting( @NonNull ChangeAnimationInfo info, @NonNull RecyclerView.ViewHolder item ) {
		if ( debugLogEnabled() ) {
			Log.d( TAG, "dispatchChangeStarting(" + item + ")" );
		}
		mItemAnimator.dispatchChangeStarting( item, ( item == info.getOldHolder() ) );
	}

	@Override
	public void dispatchFinished( @NonNull ChangeAnimationInfo info, @NonNull RecyclerView.ViewHolder item ) {
		if ( debugLogEnabled() ) {
			Log.d( TAG, "dispatchChangeFinished(" + item + ")" );
		}
		mItemAnimator.dispatchChangeFinished( item, ( item == info.getOldHolder() ) );
	}

	@Override
	public long getDuration() {
		return mItemAnimator.getChangeDuration();
	}

	@Override
	public void setDuration( long duration ) {
		mItemAnimator.setChangeDuration( duration );
	}

	@Override
	protected void onCreateAnimation( @NonNull ChangeAnimationInfo info ) {
		if ( info.getOldHolder() != null ) {
			onCreateChangeAnimationForOldItem( info );
		}

		if ( info.getNewHolder() != null ) {
			onCreateChangeAnimationForNewItem( info );
		}
	}

	@Override
	protected boolean endNotStartedAnimation( @NonNull ChangeAnimationInfo info, @Nullable RecyclerView.ViewHolder item ) {
		if ( ( info.getOldHolder() != null ) && ( ( item == null ) || ( info.getOldHolder() == item ) ) ) {
			onAnimationEndedBeforeStarted( info, info.getOldHolder() );
			dispatchFinished( info, info.getOldHolder() );
			info.clear( info.getOldHolder() );
		}

		if ( ( info.getNewHolder() != null ) && ( ( item == null ) || ( info.getNewHolder() == item ) ) ) {
			onAnimationEndedBeforeStarted( info, info.getNewHolder() );
			dispatchFinished( info, info.getNewHolder() );
			info.clear( info.getNewHolder() );
		}

		return ( info.getOldHolder() == null && info.getNewHolder() == null );
	}

	protected abstract void onCreateChangeAnimationForNewItem( ChangeAnimationInfo info );

	protected abstract void onCreateChangeAnimationForOldItem( ChangeAnimationInfo info );

	public abstract boolean addPendingAnimation( RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromX, int fromY, int toX, int toY );
}