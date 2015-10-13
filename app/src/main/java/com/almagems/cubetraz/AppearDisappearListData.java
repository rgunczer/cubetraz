package com.almagems.cubetraz;

public class AppearDisappearListData {

        int level;
        int direction;

        list<cCube*> lst_appear;
        list<cCube*> lst_disappear;

        inline void Clear()
        {
            lst_appear.clear();
            lst_disappear.clear();
        }

        inline void SetLevelAndDirection(int level, int direction)
        {
            this->level = level;
            this->direction = direction;
        }

        inline void InitAppearListFrom(list<cCube*>& lst)
        {
            lst_appear.clear();

            list<cCube*>::iterator it;
            for (it = lst.begin(); it != lst.end(); ++it)
            {
                AddAppear(*it);
            }
        }

        inline void InitDisappearListFrom(list<cCube*>& lst)
        {
            lst_disappear.clear();

            list<cCube*>::iterator it;
            for (it = lst.begin(); it != lst.end(); ++it)
            {
                AddDisappear(*it);
            }
        }

        inline void AddAppear(cCube* pCube)
        {
            assert(NULL != pCube);

            list<cCube*>::iterator it;
            for (it = lst_appear.begin(); it != lst_appear.end(); ++it)
            {
                if (pCube == *it)
                {
                    //printf("\nDuplicate(AddAppear)...");
                    return;
                }
            }

            lst_appear.push_back(pCube);
        }

        inline void AddDisappear(cCube* pCube)
        {
            assert(NULL != pCube);

            list<cCube*>::iterator it;
            for (it = lst_disappear.begin(); it != lst_disappear.end(); ++it)
            {
                if (pCube == *it)
                {
                    //printf("\nDuplicate(AddDisappear)...");
                    return;
                }
            }

            lst_disappear.push_back(pCube);
        }

        inline cCube* GetCubeFromAppearList()
        {
            cCube* pCube;
            list<cCube*>::iterator it;

            while (level > -1 && level < MAX_CUBE_COUNT)
            {
                for(it = lst_appear.begin(); it != lst_appear.end(); ++it)
                {
                    pCube = *it;

                    if (pCube->y == level)
                    {
                        lst_disappear.push_back(pCube);
                        lst_appear.remove(pCube);
                        return pCube;
                    }
                }

                level += direction;
            }

            return NULL;
        }

        inline cCube* GetCubeFromDisappearList()
        {
            cCube* pCube;
            list<cCube*>::iterator it;

            while (level > -1 && level < MAX_CUBE_COUNT)
            {
                for(it = lst_disappear.begin(); it != lst_disappear.end(); ++it)
                {
                    pCube = *it;

                    if (pCube->y == level)
                    {
                        lst_appear.push_back(pCube);
                        lst_disappear.remove(pCube);
                        return pCube;
                    }
                }

                level += direction;
            }

            return NULL;
        }



}
