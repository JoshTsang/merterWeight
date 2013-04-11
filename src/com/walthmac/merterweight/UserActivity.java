package com.walthmac.merterweight;

import com.walthmac.constant.ErrorNum;
import com.walthmac.data.User;
import com.walthmac.data.User.UserRow;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UserActivity extends BaseActivity {

	public final static String TAG = "UserActivity";
	
	private ListView userList;
	private Button addUser;
	
	private BaseAdapter userListAdapter;
	private User user = new User();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		findViews();
		setOnClickListener();
		fetchData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		title.setText("用户管理");
	}
	
	private void findViews() {
		userList = (ListView) findViewById(R.id.userList);
		addUser = (Button) findViewById(R.id.addUser);
		if (user.getCurPermission() < 3) {
			addUser.setVisibility(View.INVISIBLE);
		}
	}
	
	private void setOnClickListener() {
		addUser.setOnClickListener(addUserClicked);
	}
	
	private void fetchData() {
		new Thread() {
			public void run() {
				handler.sendEmptyMessage(user.load());
			}
		}.start();
	}

	private void refreshUserList() {
		if (userListAdapter == null) {
			userListAdapter = getAdapterInstance();
			userList.setAdapter(userListAdapter);
		} else {
			userListAdapter.notifyDataSetChanged();
		}
	}

	private BaseAdapter getAdapterInstance() {
		return new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ItemViewHolder itemViewHolder;
				if (convertView == null) {
					convertView = LayoutInflater.from(UserActivity.this)
							.inflate(R.layout.row_user, null);
					itemViewHolder = new ItemViewHolder(convertView);
					convertView.setTag(itemViewHolder);
				} else {
					itemViewHolder = (ItemViewHolder) convertView.getTag();
				}
				itemViewHolder.setTag(position);
				itemViewHolder.fillData(user.get(position));
				itemViewHolder.setOnClickListener();
				return convertView;
			}
			
			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getCount() {
				return user.size();
			}
		};
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if (msg.what >= 0) {
				refreshUserList();
			} else {
				Toast.makeText(getApplicationContext(), ErrorNum.getErrMsg(msg.what), Toast.LENGTH_LONG).show();
			}
			super.handleMessage(msg);
		}
	};
	
	private OnClickListener updateClicked = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			showUserDialog((Integer)v.getTag(), "修改用户信息");
		}
	};
	
	private OnClickListener deleteClicked = new OnClickListener() {
		
		@Override
		public void onClick(final View v) {
			new Thread() {
				public void run() {
					int ret = user.delete((Integer)v.getTag());
					if (ret >= 0) {
						handler.sendEmptyMessage(user.load());
					} else {
						handler.sendEmptyMessage(ret);
					}
				}
			}.start();
		}
	};
	private OnClickListener addUserClicked = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			showUserDialog(-1, "添加用户");
		}
	};
	
	void showUserDialog(int uid, String title) {

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = UserDialogFragment.newInstance(uid, title);
        newFragment.show(ft, "dialog");
    }
	
	class ItemViewHolder {
		TextView name;
		TextView permission;
		Button update;
		Button delete;

		public ItemViewHolder(View convertView) {
			name = (TextView) convertView.findViewById(R.id.name);
			permission = (TextView) convertView.findViewById(R.id.permission);
			update = (Button) convertView.findViewById(R.id.update);
			delete = (Button) convertView.findViewById(R.id.delete);
			
		}

		public void setOnClickListener() {
			update.setOnClickListener(updateClicked);
			delete.setOnClickListener(deleteClicked);
		}

		public void setTag(int position) {
			update.setTag(user.get(position).getId());
			delete.setTag(user.get(position).getId());
		}

		public void fillData(User.UserRow userRow) {
			name.setText(userRow.getName());
			permission.setText(userRow.getPermissionString());
			if (user.getCurPermission() <= userRow.getPermission()) {
				delete.setVisibility(View.INVISIBLE);
			}
		}
	}
	
	public static class UserDialogFragment extends DialogFragment {
        int mUid;
        String mTitle;
        User mUser;
        User.UserRow newUser;
        int curPermission;
        
        EditText name;
        EditText pwd;
        EditText pwdConfirm;
        TextView permission;
        View pwdRow;
        Activity activity;

        private void doPermissionClick() {
    		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    	    builder.setTitle("权限")
            .setItems(R.array.permissions, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
            	   User.UserRow u;
            	   if (mUid < 0) {
            		   u = newUser;
            	   } else {
            		   u= mUser.getById(mUid);
            	   }
            	   u.setPermission(which);
            	   permission.setText(u.getPermissionString());
               }
    	    });
    	    builder.show();
    	}
        

    	public void doNegativeClick() {
    		
    	}
    	
    	public int doPositiveClick() {
    		if ("".equals(name.getText().toString().trim())) {
    			Toast.makeText(getActivity(), "用户名不能为空！", Toast.LENGTH_SHORT).show();
    			return -1;
    		}
    		
    		if (pwdRow.getVisibility() == View.VISIBLE) {
	    		if ("".equals(pwd.getText().toString().trim()) || "".equals(pwdConfirm.getText().toString().trim())) {
	    			Toast.makeText(getActivity(), "密码不能为空！", Toast.LENGTH_SHORT).show();
	    			return -1;
	    		}
	    		
	    		if (!pwdConfirm.getText().toString().trim().equals(pwd.getText().toString().trim())) {
	    			Toast.makeText(getActivity(), "2次输入密码不同！", Toast.LENGTH_SHORT).show();
	    			return -1;
	    		}
    		}
    		
    		User.UserRow u;
    		if (mUid < 0) {
    			if (newUser.getPermission() < 0) {
        			Toast.makeText(getActivity(), "未选择权限！", Toast.LENGTH_SHORT).show();
        			return -1;
        		}
    			newUser.setName(name.getText().toString().trim());
    			newUser.setPwd(pwd.getText().toString());
    			u = newUser;
    		} else {
    			u = mUser.getById(mUid);
    		}
    		final UserRow addUser = u;
    		new Thread() {
    			public void run() {
    				int ret;
    				if (mUid < 0) {
    					ret = mUser.add(addUser.getName(), addUser.getPwd(), addUser.getPermission());
    				} else {
    					ret = mUser.update(addUser.getId(), addUser.getName(), addUser.getPwd(), addUser.getPermission());
    				}
    				if (ret >= 0) {
    					handler.sendEmptyMessage(mUser.load());
    				} else {
    					handler.sendEmptyMessage(ret);
    				}
    			}
    		}.start();
    		return 0;
    	}
    	
    	
    	private Handler handler = new Handler(){

    		@Override
    		public void handleMessage(Message msg) {
    			if (msg.what >= 0) {
    				//Toast.makeText(appContext, "成功添加用户！", Toast.LENGTH_LONG).show();
    				((UserActivity)activity).refreshUserList();
    			} else {
    				Toast.makeText(activity.getApplicationContext(), ErrorNum.getErrMsg(msg.what), Toast.LENGTH_LONG).show();
    			}
    			super.handleMessage(msg);
    		}
    	};
    	
        /**
         * Create a new instance of MyDialogFragment, providing "num"
         * as an argument.
         */
        static UserDialogFragment newInstance(int uid, String title) {
        	UserDialogFragment f = new UserDialogFragment();

            Bundle args = new Bundle();
            args.putInt("uid", uid);
            args.putString("title",	title);
            f.setArguments(args);

            return f;
        }
        
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mUid = getArguments().getInt("uid");
            mTitle = getArguments().getString("title");
            mUser = new User();
            if (mUid < 0) {
            	newUser = mUser.newInstanceOfUserRow();
            }
            curPermission = mUser.getCurPermission();
            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog);
            activity = getActivity();
        }

//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                Bundle savedInstanceState) {
//        	//getDialog().setTitle(mTitle);
//            View v = inflater.inflate(R.layout.user_dialog, container, false);
//            
//            name = (TextView) v.findViewById(R.id.name);
//            name.setText(mUser.getById(mUid).getName());
//            return v;
//        }
        
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
        	LayoutInflater inflater = getActivity().getLayoutInflater();
        	 
        	View v = inflater.inflate(R.layout.user_dialog, null);
        	name = (EditText) v.findViewById(R.id.name);
        	name.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        	pwd = (EditText) v.findViewById(R.id.pwd);
        	pwd.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        	pwdConfirm = (EditText) v.findViewById(R.id.pwdConfirm);
        	pwdConfirm.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        	permission = (TextView) v.findViewById(R.id.permission);
        	pwdRow = v.findViewById(R.id.pwdRow);
        	if (mUid >= 0) {
	        	name.setText(mUser.getById(mUid).getName());
	        	name.setFocusable(false);
	        	permission.setText(mUser.getById(mUid).getPermissionString());
	        	
	        	if (mUser.getCurUID() != mUid) {
	        		pwdRow.setVisibility(View.GONE);
	        		v.findViewById(R.id.pwdConfirmRow).setVisibility(View.GONE);
	        	}
        	}
        	
        	//TODO define permisson
        	if (mUser.getCurPermission() == 3)  {
	        	permission.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						doPermissionClick();
					}
				});
        	}
        	
        	final AlertDialog d = new AlertDialog.Builder(getActivity())
            		.setView(v)
                    .setTitle(mTitle)
                    .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                
                            }
                        }
                    )
                    .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                doNegativeClick();
                            }
                        }
                    )
                    .setCancelable(false)
                    .create();
        	
        	d.setOnShowListener(new DialogInterface.OnShowListener() {

        	    @Override
        	    public void onShow(DialogInterface dialog) {

        	        Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
        	        b.setOnClickListener(new View.OnClickListener() {

        	            @Override
        	            public void onClick(View view) {
        	            	if (doPositiveClick() >= 0) {
        	            		d.dismiss();
        	            	}
        	            }
        	        });
        	    }
        	});
        	return d;
        }
    }
}
