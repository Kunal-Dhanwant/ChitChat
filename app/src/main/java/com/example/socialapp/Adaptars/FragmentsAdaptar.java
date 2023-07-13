package com.example.socialapp.Adaptars;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.socialapp.Fragments.CallsFragmen;
import com.example.socialapp.Fragments.ChatsFragment;
import com.example.socialapp.Fragments.StatusFragments;

public class FragmentsAdaptar extends FragmentPagerAdapter {
    public FragmentsAdaptar(@NonNull FragmentManager fm) {
        super(fm);
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {


        switch (position){
            case 0: return new ChatsFragment();
            case 1: return new StatusFragments();
            case 2: return new CallsFragmen();

            default: return new ChatsFragment();
        }



    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title =null;

        if(position==0){
            title ="CHATS";
        }
        if(position==1){
            title ="STATUS";
        }
        if(position==2){
            title ="CALLS";
        }

        return title;
    }
}
