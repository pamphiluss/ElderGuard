
package com.syd.elderguard.manager;

import com.syd.elderguard.entity.EgUser;


public class UserManager {


    private static UserManager sInstance;


    private EgUser mUser;

    private UserManager() {

    }

    public static UserManager getInstance() {
        if (sInstance == null) {
            synchronized (UserManager.class) {
                if (sInstance == null) {
                    sInstance = new UserManager();
                }
            }
        }
        return sInstance;
    }

    public void selectUser(EgUser user) {
        mUser = user;
    }

    public EgUser getUser() {
        return mUser;
    }

    public String getSelectUserName() {
        if (mUser != null) {
            return mUser.getUsername();
        } else {
            return "未选择用户！";
        }
    }

    public void clear() {
        mUser = null;
    }

}
