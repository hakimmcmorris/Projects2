using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CameraScript : MonoBehaviour
{

    #region player
    [SerializeField]
    [Tooltip("keeps track of player")]
    private GameObject Player;
    public float zoom;
    #endregion


    #region Unityfunc
    // Update is called once per frame
    void Update()
    {
        Vector3 moved = new Vector3(Player.transform.position.x, Player.transform.position.y, zoom);
        transform.position = moved;
    }
    #endregion
}
