using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class Player : MonoBehaviour
{
    #region Movment_vars
    [SerializeField]
    [Tooltip("How fast player moves")]
    private float move_speed;
    private Vector2 currdir;
    private Vector3 mousepos;
    private Vector2 looking;
    private float x_input;
    private float y_input;
    private bool dodging;
    [SerializeField]
    private Vector2 last_visited;
    [SerializeField]
    [Tooltip("How fast you roll")]
    private float dodge_speed;
    [SerializeField]
    [Tooltip("How long you roll")]
    private float dodge_time;
    #endregion

    #region Unity_vars
    Rigidbody2D PlayerRB;
    #endregion

    #region Health_vars
    [SerializeField]
    [Tooltip("How much health the player should have")]
    private float max_health;
    private float curr_health;
    #endregion

    #region UI_vars
    [SerializeField]
    [Tooltip("holds the slider associated with health")]
    private Slider HPSlider;
    [SerializeField]
    private Text Ammo;
    #endregion

    #region Weapon vars
    public Weapons CurrentWeapon;
    public int weaponIndex;
    public List<Weapons> allWeapons;
    #endregion

    #region Unity_funcs
    private void Awake()
    {
        PlayerRB = GetComponent<Rigidbody2D>();
        curr_health = max_health;
        CurrentWeapon.last_fired = CurrentWeapon.fire_rate;
        transform.position = last_visited;
        HPSlider.value = curr_health / max_health;
        CurrentWeapon.Awake();
        weaponIndex = 0;

         Ammo.text = "Ammo:" + CurrentWeapon.currentAmmo.ToString();
         

    }
    private void Update()
    {
        x_input = Input.GetAxisRaw("Horizontal");
        y_input = Input.GetAxisRaw("Vertical");
        if(!dodging)
        {
            move();
        }
        pointing();
        CurrentWeapon.last_fired += Time.deltaTime;
        if (Input.GetButton("Fire1"))
        {
            if(CurrentWeapon.last_fired > CurrentWeapon.fire_rate && CurrentWeapon.currentAmmo > 0)
            {
                Attack();
                CurrentWeapon.currentAmmo -= 1;
                Ammo.text = "Ammo:" + CurrentWeapon.currentAmmo.ToString();
                CurrentWeapon.last_fired = 0f;
            }
        }

        if (Input.GetButtonDown("Fire2"))
        {
            Debug.Log("Switch bullets");
            CurrentWeapon.SwitchBullet();
        }

        if (Input.GetKeyDown("z") && !dodging)
        {
            StartCoroutine(dodge());
        }
        if (Input.GetKeyDown("t"))
        {
            changeWeapons();
        }
    }
    #endregion

    #region lookingfunc
    private void pointing()
    {
        mousepos = Camera.main.ScreenToWorldPoint(Input.mousePosition);
        looking = new Vector2((mousepos.x - transform.position.x), (mousepos.y - transform.position.y));
        transform.up = looking;
    }
    #endregion

    #region movefunc
    private void move()
    {
        //based on x and y input decide how the player should move through their velocity
        // you may want to normilize the vector you make
        PlayerRB.velocity = new Vector2(x_input, y_input).normalized * move_speed;
    }

    private IEnumerator dodge()
    {
        dodging = true;
        PlayerRB.velocity = looking.normalized * -1 * dodge_speed;
        yield return new WaitForSeconds(dodge_time);
        PlayerRB.velocity = Vector2.zero;
        yield return new WaitForSeconds(dodge_time / 2);
        dodging = false;
    }
    #endregion

    #region Attack_funcs

    private void Attack()
    {
        //Debug.Log("live clean");
        CurrentWeapon.Attack(transform.position, looking);
    }

    void changeWeapons() 
    {
        Debug.Log("changing");
        weaponIndex++;
        if(weaponIndex >= allWeapons.Count) 
        {
            weaponIndex = 0;
        }
        CurrentWeapon = allWeapons[weaponIndex];
    }

    #endregion

    #region Health_funcs
    public void TakeDamage(float dmg)
    {
        curr_health -= dmg;
        //Debug.Log("curr_health");
        if (curr_health <= 0)
        {
            Awake();
        }
        HPSlider.value = curr_health / max_health;
    }

    public void SetSpawn(Vector2 checkpoint)
    {
        last_visited = checkpoint;
    }
    #endregion
}
