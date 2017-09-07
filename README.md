# WipperSwitch
滑动开关、支持主题修改

		<com.yumpkimx.wipperswitchlib.WipperSwitchView
			android:id="@+id/wipperswitch"
			android:layout_width="80dp"
			android:layout_height="40dp"
			android:layout_marginRight="8dp"
			app:layout_constraintRight_toRightOf="parent"
			android:layout_marginLeft="8dp"
			app:layout_constraintLeft_toLeftOf="parent"
			wipper:frontColor="#ff118955"
			wipper:wipperColor="#ffffffff"
			wipper:isOpen="true"
			app:layout_constraintTop_toTopOf="parent"
			android:layout_marginTop="8dp"
			app:layout_constraintBottom_toBottomOf="parent"
			android:layout_marginBottom="8dp"/>
			
注意：需添加命名空间xmlns:wipper="http://schemas.android.com/apk/res/com.yumpkimx.example.wipperswitch"

控件注册开关状态改变监听器

			final WipperSwitchView wipperSwitchView = (WipperSwitchView) findViewById(R.id.wipperswitch);
			wipperSwitchView.addOnSwitchStateChangeListener(new WipperSwitchView.OnSwitchStateChangeListener() {
					@Override
					public void open() {
					// do something
					}
					@Override
					public void close() {
					//do something
					}
			});
				
				
四个自定义属性可设置:

			wipper:backgroundColor  //开关关闭时背景色（默认灰色）
			wipper:frontColor		//开关打开时背景色（默认橘色）
			wipper:wipperColor //滑块颜色 （默认白色）
			wipper:isOpen			//打开还是关闭（不设置默认关闭）

控件宽高可以不设置：

			android:layout_width="wrapcontent"
			android:layout_height="wrapcontent"
			
也可以设置成具体的尺寸，建议宽高设置比例为2：1
