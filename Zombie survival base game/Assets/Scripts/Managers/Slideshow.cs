using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class Slideshow : MonoBehaviour
{
    // Start is called before the first frame update
    public SpriteRenderer slideshow;
    public List<Sprite> slides;
    public int index;
    public string nextScene;

    void Start()
    {
        index = 0;
    }

    // Update is called once per frame
    void Update()
    {
        if(Input.GetMouseButtonUp(0))
        {
            index++;
            if(index == slides.Count)
            {
                SceneManager.LoadScene(nextScene);
            } 
            slideshow.sprite = slides[index];
        }
    }
}
