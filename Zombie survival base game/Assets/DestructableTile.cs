using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Tilemaps;

public class DestructableTile : MonoBehaviour
{
    public Tilemap destructableTilemap;
    // Start is called before the first frame update
    void Start()
    {
        destructableTilemap = GetComponent<Tilemap>();
    }


    // private void OnCollisionEnter2D(Collision2D collision) {
    //     Debug.Log("Collision: " + collision.gameObject.tag);
    //     Debug.Log(collision.gameObject.name);
    //     if (collision.gameObject.CompareTag("Bullet")) {
    //         Vector3 hitPosition = Vector2.zero;
    //         foreach (ContactPoint2D hit in collision.contacts) {
    //             hitPosition.x = hit.point.x - 0.01f * hit.normal.x;
    //             hitPosition.y = hit.point.y - 0.01f * hit.normal.y;
    //             destructableTilemap.SetTile(destructableTilemap.WorldToCell(hitPosition), null);
    //         }
    //     }
    // }
    public void destruct(Transform transform, Vector3 velocity) {
        Debug.Log("Transform: " + transform);
        Debug.Log("Velocity: " + velocity);
        float scale = (transform.localScale.x + transform.localScale.y)/2;
        Vector3 hit_position = transform.position + scale * velocity.normalized;
        destructableTilemap.SetTile(destructableTilemap.WorldToCell(hit_position), null);

    }
}
