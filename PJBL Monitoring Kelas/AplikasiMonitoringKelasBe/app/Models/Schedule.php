<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class Schedule extends Model
{
    use HasFactory;

    protected $fillable = [
        'hari',
        'kelas',
        'mata_pelajaran',
        'guru_id',
        'jam_mulai',
        'jam_selesai',
        'ruang'
    ];

    protected $casts = [
        'jam_mulai' => 'datetime:H:i',
        'jam_selesai' => 'datetime:H:i',
    ];

    /**
     * Relasi ke model Teacher (guru)
     */
    public function teacher()
    {
        return $this->belongsTo(Teacher::class, 'guru_id');
    }

    /**
     * Alias untuk relasi teacher (untuk kompatibilitas API)
     */
    public function guru()
    {
        return $this->belongsTo(Teacher::class, 'guru_id');
    }
}
