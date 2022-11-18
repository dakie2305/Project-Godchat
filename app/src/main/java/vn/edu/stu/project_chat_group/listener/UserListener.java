package vn.edu.stu.project_chat_group.listener;

import vn.edu.stu.project_chat_group.models.User;

public interface UserListener {
    //khi ấn vào user nàoo thì sẽ truyền user đó vào
    //đây là custom onUserClick
    void onUserClick(User user);
}
