import '@/styles/globals.css'
import { useRouter } from 'next/router'
import TopBar from '@/components/TopBar';
import Authentication from '@/authentication/Authentication';
import Head from 'next/head'


export default function App({ Component, pageProps }) {
  const router = useRouter();

  return (
    <>
    <Head>
        <title>TrainingApp</title>
        <meta name="viewport" content="width=device-width, initial-scale=1" />
      </Head>
    <Authentication>
      {!router.asPath.startsWith("/Login") && <TopBar></TopBar>}
      <Component {...pageProps} />
    </Authentication>
    </>);
}
