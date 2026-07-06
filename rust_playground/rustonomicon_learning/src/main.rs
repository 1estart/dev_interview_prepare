#[forbid(unsafe_code)]
fn main() {
    println!("Hello, world!");
    unsafe {
        unsafe_fn();
    };
}

unsafe fn unsafe_fn() {
    println!("Hello, world!");
}
