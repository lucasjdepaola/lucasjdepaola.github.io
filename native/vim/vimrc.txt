let mapleader = " "

nmap <leader>w :w!<cr>
nmap <leader>x :x!<cr>
nmap <leader>l :source ~/_ideavimrc<cr>
nmap ; :
nmap <leader>r :action Run<cr>
nmap <leader>g :e Main.java<cr>:action Run<cr>
nmap <leader>f :action NewFile<cr>
nmap J <c-d>zz
nmap K <c-u>zz
vmap <leader>c :action ReformatCode<cr>
nmap <leader>d :action Debug<cr>
nmap j j
nmap k k
nmap n nzz
nmap <leader>e <F2>

imap jk <Esc>l
set timeoutlen=1000

imap jf System.out.println(
set timeoutlen=1000

set visualbell
set smartcase
set incsearch
