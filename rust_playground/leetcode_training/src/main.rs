fn main() {
    println!("Hello, world!");
}

fn block_matches(block: &[i32], target: &[i32]) -> bool {
    let k = block.len();

    if k == 1 {
        return block[0] == target[0];
    }

    let mut breaks = 0;
    let mut start = 0;

    for i in 0..k - 1 {
        if block[i] > block[i + 1] {
            breaks += 1;
            start = i + 1;

            if breaks > 1 {
                return false;
            }
        }
    }

    if block[k - 1] > block[0] {
        breaks += 1;
        start = 0;

        if breaks > 1 {
            return false;
        }
    }

    // block[start..] ++ block[..start]
    let (left, right) = block.split_at(start);

    right.iter().chain(left.iter()).eq(target.iter())
}

fn sortable_with_k(nums: &[i32], sorted: &[i32], k: usize) -> bool {
    nums.chunks_exact(k)
        .zip(sorted.chunks_exact(k))
        .all(|(block, target)| block_matches(block, target))
}

pub fn sortable_integers(nums: Vec<i32>) -> i32 {
    let n = nums.len();

    let mut sorted = nums.clone();
    sorted.sort_unstable();

    let mut answer = 0i64;

    for k in 1..=n {
        if n % k == 0 && sortable_with_k(&nums, &sorted, k) {
            answer += k as i64;
        }
    }

    answer as i32
}
