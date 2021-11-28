using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class GameManager : MonoBehaviour
{
    public static GameManager instance = null;
    private int total_scr;
    private Text score;


    #region Unity_functions
    private void Awake()
    {
        if(instance == null)
        {
            instance = this;
        }else if(instance != this)
        {
            Destroy(this.gameObject);
        }
        DontDestroyOnLoad(gameObject);
        find_score();
        

    }
    #endregion

    #region Scene_transisitons
    public void StartGame()
    {
        SceneManager.LoadScene("LevelOne");
        total_scr = 0;
    }

    public void MainMenu()
    {
        Debug.Log("benji's button");
        SceneManager.LoadScene("Main Menu");

    }

    public void Sewers()
    {
        SceneManager.LoadScene("sewersOfLondon");
    }
    #endregion

    #region Score_func
    public void score_update(int add)
    {
        total_scr += add;
        score.text ="score: " + total_scr.ToString();
    }

    private void score_reset()
    {
        total_scr = 0;
    }

    private void find_score()
    {
        Text[] all_strings;
        all_strings = FindObjectsOfType<Text>();
        foreach (Text t in all_strings)
        {
            if (t != null && t.CompareTag("Score"))
            {
                score = t;
                score.text = "score:" + total_scr.ToString();
            }
        }


        
    }
    #endregion
}
