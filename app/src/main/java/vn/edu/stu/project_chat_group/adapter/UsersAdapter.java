package vn.edu.stu.project_chat_group.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.stu.project_chat_group.databinding.ItemContainerUserBinding;
import vn.edu.stu.project_chat_group.models.User;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewholder>{

    private final List<User> users; //tạo một list chứa các clas User

    public UsersAdapter(List<User> users) {
        this.users = users;
    }       //sử dụng adapter tuỳ chỉnh là UsersAdapter, truyền vào list user

    @NonNull
    @Override
    public UserViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerUserBinding itemContainerUserBinding = ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserViewholder(itemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewholder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() { //trả về tổng số lượng user
        return users.size();
    } //lấy số lượng user trong đây

    class UserViewholder extends RecyclerView.ViewHolder{

        ItemContainerUserBinding binding;
        //ItemContainerUserBinding sẽ lấy layout item_container_user
        UserViewholder(ItemContainerUserBinding itemContainerUserBinding){
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;
        }



        void setUserData(User user){    //dùng để đưa tên, đưa username và đưa ảnh đại diện của người dùng lên
            binding.tvName.setText(user.name);
            binding.tvUser.setText( "@"+user.username);
            binding.imageProfileUser.setImageBitmap(getUserImage(user.image));  //để có thể lấy được ảnh đại diện, cần phải giải đoạn mã hoá ảnh đại diện
        }
    }
    private Bitmap getUserImage(String encodedImage){ //vì ảnh đại diện của user là dạng chuỗi string đã mã hoá, ta cần giải mã hoá để lấy ra ảnh đại diện
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

    }

}
