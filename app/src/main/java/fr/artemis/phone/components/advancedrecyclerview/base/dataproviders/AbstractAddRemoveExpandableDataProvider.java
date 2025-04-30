package fr.artemis.phone.components.advancedrecyclerview.base.dataproviders;


import fr.artemis.phone.dto.CRMPhoneEquipementDTO;

public abstract class AbstractAddRemoveExpandableDataProvider {

	public static abstract class BaseData {

		public abstract String getText();
	}

	public static abstract class GroupData extends BaseData {

		public abstract long getGroupId();
	}

	public static abstract class EquipementData extends GroupData {

		public abstract CRMPhoneEquipementDTO getEquipement();
	}

	public static abstract class ChildData extends BaseData {

		public abstract long getChildId();
	}

	public abstract int getGroupCount();

	public abstract int getChildCount( int groupPosition );

	public abstract GroupData getGroupItem( int groupPosition );

	public abstract ChildData getChildItem( int groupPosition, int childPosition );

	public abstract void addGroupItem( int groupPosition );

	public abstract void addChildItem( int groupPosition, int childPosition );

	public abstract void removeGroupItem( int groupPosition );

	public abstract void removeChildItem( int groupPosition, int childPosition );

	public abstract void clear();

	public abstract void clearChildren( int groupPosition );
}