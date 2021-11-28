using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GunZombie : MonoBehaviour
{
    [SerializeField] private Weapons CurrentWeapon;
    private Vector2 looking;
    private Player player;
    void Awake() 
    {
        player = GameObject.FindGameObjectWithTag("Player").GetComponent<Player>();
    }
    void Update() 
    {
        CurrentWeapon.last_fired += Time.deltaTime;
        if(CurrentWeapon.last_fired > CurrentWeapon.fire_rate)
        {
            look();
            CurrentWeapon.Attack(transform.position, looking); 
            CurrentWeapon.last_fired = 0f;
        }
    }
    void look() 
    {
       looking = new Vector2((player.transform.position.x - transform.position.x), (player.transform.position.y - transform.position.y));    
    }
}
