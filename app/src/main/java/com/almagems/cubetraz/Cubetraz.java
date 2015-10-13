package com.almagems.cubetraz;

public class Cubetraz {


    // v1.0
    struct LevelData
    {
        int star_count; // -1 = locked, 0 = enabled, 1 = one star, 2 = two star, 3 = three star
        int move_count;
        time_t time_count;
    };

    // v1.1
    struct LevelData11
    {
        int stars;
        int moves;
        bool solved;

    void Reset()
    {
        stars = LEVEL_LOCKED;
        moves = 0;
        solved = false;
    }
};

#define MAX_LEVELS 61

class cCubetraz
{

    private:

    // v1.1
    static LevelData11 ar_levels_easy[MAX_LEVELS];
    static LevelData11 ar_levels_normal[MAX_LEVELS];
    static LevelData11 ar_levels_hard[MAX_LEVELS];

    public:

    static void Init();

    static void CreateOldSaveGameFile();

    static void Convert();

    static void Save();
    static void Load();

    static int GetStarCount();

    static int GetStarsEasy(const int level_number)     { return ar_levels_easy[level_number-1].stars;      }
    static int GetStarsNormal(const int level_number)   { return ar_levels_normal[level_number-1].stars;    }
    static int GetStarsHard(const int level_number)     { return ar_levels_hard[level_number-1].stars;      }

    static bool GetSolvedEasy(const int level_number)   { return ar_levels_easy[level_number-1].solved;     }
    static bool GetSolvedNormal(const int level_number) { return ar_levels_normal[level_number-1].solved;   }
    static bool GetSolvedHard(const int level_number)   { return ar_levels_hard[level_number-1].solved;     }

    static void SetStarsEasy(const int level_number, const int stars)
    {
        int index = level_number - 1;

        if (stars > ar_levels_easy[index].stars)
            ar_levels_easy[index].stars = stars;
    }

    static void SetMovesEasy(const int level_number, const int moves)       { ar_levels_easy[level_number-1].moves = moves;     }
    static void SetSolvedEasy(const int level_number, const bool solved)    { ar_levels_easy[level_number-1].solved = solved;   }

    static void SetStarsNormal(const int level_number, const int stars)
    {
        int index = level_number - 1;

        if (stars > ar_levels_normal[index].stars)
            ar_levels_normal[index].stars = stars;
    }

    static void SetMovesNormal(const int level_number, const int moves)     { ar_levels_normal[level_number-1].moves = moves;   }
    static void SetSolvedNormal(const int level_number, const bool solved)  { ar_levels_normal[level_number-1].solved = solved; }

    static void SetStarsHard(const int level_number, const int stars)
    {
        int index = level_number - 1;

        if (stars > ar_levels_hard[index].stars)
            ar_levels_hard[index].stars = stars;
    }

    static void SetMovesHard(const int level_number, const int moves)       { ar_levels_hard[level_number-1].moves = moves;     }
    static void SetSolvedHard(const int level_number, const bool solved)    { ar_levels_hard[level_number-1].solved = solved;   }


    // v1.1
    LevelData11 cCubetraz::ar_levels_easy[61];
    LevelData11 cCubetraz::ar_levels_normal[61];
    LevelData11 cCubetraz::ar_levels_hard[61];


    void cCubetraz::Init()
{
    for (int i = 0; i < 61; ++i)
    {
        ar_levels_easy[i].Reset();
        ar_levels_normal[i].Reset();
        ar_levels_hard[i].Reset();
    }
}

    #pragma mark - CreateOldSaveGameFile

    void cCubetraz::CreateOldSaveGameFile()
{
    LevelData arLevels[46];

    for (int i = 0; i < 46; ++i)
    {
        arLevels[i].star_count = 1; //RANDOM_INT(-1, 3);
        //printf("\nstars[%d]: %d", i, arLevels[i].star_count);
    }

    FILE* fp;

    fp = engine->m_resourceManager->GetFilePointerForWrite(SAVE_GAME_FILE);
    if (fp)
    {
        fwrite(arLevels, 1, sizeof(arLevels), fp);
        fclose(fp);
    }
}

    #pragma mark - Convert

    void cCubetraz::Convert()
{
    FILE* fp = engine->m_resourceManager->GetFilePointerForRead(SAVE_GAME_FILE_EASY);

    if (fp)
    {
        fclose(fp);
        return; // new savegame file found, nothing to convert!
    }

    // convert old savegame file to new version

    for (int i = 0; i < 60; ++i)
    {
        ar_levels_easy[i].stars = LEVEL_LOCKED;
        ar_levels_easy[i].moves = 0;
        ar_levels_easy[i].solved = false;

        ar_levels_normal[i].stars = LEVEL_LOCKED;
        ar_levels_normal[i].moves = 0;
        ar_levels_normal[i].solved = false;

        ar_levels_hard[i].stars = LEVEL_LOCKED;
        ar_levels_hard[i].moves = 0;
        ar_levels_hard[i].solved = false;
    }

    fp = engine->m_resourceManager->GetFilePointerForRead(SAVE_GAME_FILE); // old savegame v1.0

    if (fp)
    {
        LevelData arLevels[46];

        fread(arLevels, 1, sizeof(arLevels), fp);
        fclose(fp);

        // -1 locked
        // 0 unlocked
        // 1 star
        // 2 star
        // 3 star

        for (int i = 0; i < 45; ++i) // do convert here
        {
            ar_levels_normal[i].stars = arLevels[i+1].star_count;
            ar_levels_normal[i].moves = arLevels[i+1].move_count;
        }

        if (arLevels[45].star_count > 0) // if player solved the last (level 45) unlock next one (level 46)
            ar_levels_normal[45].stars = LEVEL_UNLOCKED;
    }

    // unlock first levels
    if (LEVEL_LOCKED == ar_levels_easy[0].stars)
        ar_levels_easy[0].stars = LEVEL_UNLOCKED;

    if (LEVEL_LOCKED == ar_levels_normal[0].stars)
        ar_levels_normal[0].stars = LEVEL_UNLOCKED;

    if (LEVEL_LOCKED == ar_levels_hard[0].stars)
        ar_levels_hard[0].stars = LEVEL_UNLOCKED;

    Save();
}

    #pragma mark - Save

    void cCubetraz::Save()
{
    FILE* fp;

    fp = engine->m_resourceManager->GetFilePointerForWrite(SAVE_GAME_FILE_EASY);
    if (fp)
    {
        fwrite(ar_levels_easy, 1, sizeof(ar_levels_easy), fp);
        fclose(fp);
    }

    fp = engine->m_resourceManager->GetFilePointerForWrite(SAVE_GAME_FILE_NORMAL);
    if (fp)
    {
        fwrite(ar_levels_normal, 1, sizeof(ar_levels_normal), fp);
        fclose(fp);
    }

    fp = engine->m_resourceManager->GetFilePointerForWrite(SAVE_GAME_FILE_HARD);
    if (fp)
    {
        fwrite(ar_levels_hard, 1, sizeof(ar_levels_hard), fp);
        fclose(fp);
    }
}

    #pragma mark - Save

    void cCubetraz::Load()
{
    FILE* fp;

    fp = engine->m_resourceManager->GetFilePointerForRead(SAVE_GAME_FILE_EASY);
    if (fp)
    {
        fread(ar_levels_easy, 1, sizeof(ar_levels_easy), fp);
        fclose(fp);
    }

    fp = engine->m_resourceManager->GetFilePointerForRead(SAVE_GAME_FILE_NORMAL);
    if (fp)
    {
        fread(ar_levels_normal, 1, sizeof(ar_levels_normal), fp);
        fclose(fp);
    }

    fp = engine->m_resourceManager->GetFilePointerForRead(SAVE_GAME_FILE_HARD);
    if (fp)
    {
        fread(ar_levels_hard, 1, sizeof(ar_levels_hard), fp);
        fclose(fp);
    }
}

//		// dump loaded data
//		char str_time[32];
//
//		for (int i = 0; i < 46; ++i)
//		{
//			GetTimeString(str_time, m_arLevels[i].time_count);
//			printf("\n%d, Stars: %d, Moves: %d, Time: %s", i, m_arLevels[i].star_count, m_arLevels[i].move_count, str_time);
//		}

    int cCubetraz::GetStarCount()
{
    int stars = 0;

    for (int i = 0; i < 60; ++i)
    {
        if (ar_levels_easy[i].stars > 0)
            stars += ar_levels_easy[i].stars;

        if (ar_levels_normal[i].stars > 0)
            stars += ar_levels_normal[i].stars;

        if (ar_levels_hard[i].stars > 0)
            stars += ar_levels_hard[i].stars;
    }

    return stars;
}


}
